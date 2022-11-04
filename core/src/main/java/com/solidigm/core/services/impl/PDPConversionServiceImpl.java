package com.solidigm.core.services.impl;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.solidigm.core.services.PDPConversionService;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component(service = PDPConversionService.class, immediate = true)
public class PDPConversionServiceImpl implements PDPConversionService {

    private static ArrayList<String> LANGUAGE_PATHS = new ArrayList<>(Arrays.asList(
            "/content/solidigm/language-masters/en", "/content/solidigm/language-masters/ja",
            "/content/solidigm/language-masters/zh", "/content/solidigm/language-masters/de",
            "/content/solidigm/language-masters/ko"
    ));

    private static String TEMPLATE_PATH = "/conf/solidigm/settings/wcm/templates/product-detail-page--pdp-1";

    private static String OLD_TEMPLATE = "/conf/solidigm/settings/wcm/templates/product-detail-page--pdp-";

    private static String TOP_CONTAINER = "container_2089249899";

    private static String TEMP_PAGE_NAME = "tempPage";

    private static ArrayList<String> PRODUCT_PATHS = new ArrayList<>(Arrays.asList(
            "/products/data-center/d7", "/products/data-center/d5", "/products/data-center/d3",
            "/products/client/d6/p44", "/products/client/d6/p31",
            "/products/client/d6/p41", "/products/client/d6/670p",
            "/products/client/d6/660p", "/products/data-center/ssd-d7-series", "/products/data-center/ssd-d5-series",
            "/products/data-center/ssd-3-series", "/products/client-product-category/d6/p44",
            "/products/client-product-category/d6/p31", "/products/client-product-category/d6/p41",
            "/products/client-product-category/d6/p670", "/products/client-product-category/d6/p660",
            "/products/client/d6"
    ));

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final Logger logger = LoggerFactory.getLogger(PDPConversionServiceImpl.class);

    private void moveContent(Session session, String srcPath, String destPath){
        // Move Node
        try {
            session.removeItem(destPath);
            session.move(srcPath, destPath);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceResolver getResolver(){
        // Get resource resolver
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            logger.error("Unable to sign in Knowledge Articles Service User");
            return null;
        }
        return resolver;
    }

    @Override
    public void convertPDP() {

        // Get resource resolver
        ResourceResolver resolver = getResolver();
        if (resolver == null){
            return;
        }

        // Get variables that stay the same across the for loops
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Session session = resolver.adaptTo(Session.class);

        for (String root : LANGUAGE_PATHS){

            for (String path : PRODUCT_PATHS){

                // Get page resource
                Resource resource = resolver.getResource(root + path);
                if (resource == null){
                    continue;
                }

                // Iterate through children pages
                for (Resource child : resource.getChildren()){
                    // Skip parent's jcr:content or any child page that shouldn't be changed
                    if (!child.getResourceType().equals("cq:Page")
                            || !child.getChild("jcr:content").getValueMap().get("cq:template", String.class).equals(OLD_TEMPLATE)){
                        continue;
                    }

                    // Create temporary new page to copy content onto
                    String newPagePath = root + path + "/" + TEMP_PAGE_NAME;
                    try {
                        pageManager.create(root + path, TEMP_PAGE_NAME,
                                TEMPLATE_PATH, TEMP_PAGE_NAME);
                    } catch (WCMException e) {
                        throw new RuntimeException(e);
                    }

                    Resource newPage = resolver.getResource(newPagePath);

                    // Get destination paths for cloning
                    String topDestPath = newPagePath + "/jcr:content/root/" + TOP_CONTAINER + "/";
                    String bottomDestPath = newPagePath + "/jcr:content/root/container/";

                    // Iterate through source page's content
                    for (Resource parentContent : child.getChild("jcr:content/root").getChildren()){
                        // Skip header/footer
                        if (parentContent.getName().equals("header") || parentContent.getName().equals("footer")){
                            continue;
                        }
                        // Decide if it goes in top container or bottom container
                        if (parentContent.getName().equals("productphotogallery")
                                || parentContent.getName().equals("productoverview")){
                            moveContent(session, parentContent.getPath(),
                                    topDestPath + parentContent.getName());
                        }
                        else {
                            moveContent(session, parentContent.getPath(),
                                    bottomDestPath + parentContent.getName());
                        }
                    }

                    try {
                        // Reassign node properties
                        ValueMap sourceMap = child.getChild("jcr:content/root").getValueMap();
                        Node destRoot = newPage.getChild("jcr:content/root").adaptTo(Node.class);
                        Node sourcePage = child.getChild("jcr:content").adaptTo(Node.class);
                        destRoot.setProperty("jcr:mixinTypes", sourceMap.get("jcr:mixinTypes", String.class));
                        sourcePage.setProperty("cq:template", TEMPLATE_PATH);

                        // Move updated root back, delete temporary page
                        session.removeItem(child.getPath() + "/jcr:content/root");
                        session.move(newPagePath + "/jcr:content/root", child.getPath() + "/jcr:content/root");
                        session.removeItem(newPagePath);
                        session.save();
                    } catch (RepositoryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public String checkPages(){

        // Get resource resolver
        ResourceResolver resolver = getResolver();
        if (resolver == null){
            return null;
        }

        String sanityCheck = "***********Converted pages:**********\n\n";

        for (String root : LANGUAGE_PATHS){
            for (String path : PRODUCT_PATHS) {
                // Get page resource
                Resource resource = resolver.getResource(root + path);
                if (resource == null){
                    continue;
                }

                // Iterate through children pages
                for (Resource child : resource.getChildren()){
                    // Skip parent's jcr:content
                    if (!child.getResourceType().equals("cq:Page")){
                        continue;
                    }
                    if (!child.getChild("jcr:content").getValueMap().get("cq:template", String.class).equals(TEMPLATE_PATH)
                            && !child.getChild("jcr:content").getValueMap().get("cq:template", String.class).equals(OLD_TEMPLATE)){
                        continue;
                    }
                    boolean correctTemplate = child.getChild("jcr:content").getValueMap().get("cq:template", String.class).equals(TEMPLATE_PATH);
                    boolean linkInPath = (child.getChild("jcr:content/root/container/quicklinks/links/item0") != null);
                    boolean productImageInPath = (child.getChild("jcr:content/root/container_2089249899/productphotogallery") != null);
                    if (correctTemplate && linkInPath && productImageInPath){
                        sanityCheck += ("Successful: " + child.getPath() + "\n");
                    }
                    else {
                        sanityCheck += "*****UNSUCCESSFUL: " + child.getPath() + "\n";
                    }
                }
            }
        }
        sanityCheck += "Check complete.";
        return sanityCheck;
    }
}
