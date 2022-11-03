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

    private static String TOP_CONTAINER = "container_2089249899";

    /**
     * Paths I have in my content hierarchy but don't appear to be live in other languages
     *  "/products/data-center/ssd-d7-series", "/products/data-center/ssd-d5-series",
     *             "/products/data-center/ssd-3-series", "/products/client-product-category/d6/p44",
     *             "/products/client-product-category/d6/p31", "/products/client-product-category/d6/p41",
     *             "/products/client-product-category/d6/p670", "/products/client-product-category/d6/p660",
     */

    private static ArrayList<String> PRODUCT_PATHS = new ArrayList<>(Arrays.asList(
            "/products/data-center/d7", "/products/data-center/d5", "/products/data-center/d3",
            "/products/client/d6/p44", "/products/client/d6/p31",
            "/products/client/d6/p41", "/products/client/d6/670p",
            "/products/client/d6/660p"
    ));

    @Reference
    private ResourceResolverFactory resolverFactory;

    private static final Logger logger = LoggerFactory.getLogger(PDPConversionServiceImpl.class);

    @Override
    public void convertPDP() {

        // Get resource resolver
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, "knowledgeArticlesServiceUser");
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            logger.error("Unable to sign in Knowledge Articles Service User");
            return;
        }

        // Get variables that stay the same across the for loops
        PageManager page = resolver.adaptTo(PageManager.class);
        Session session = resolver.adaptTo(Session.class);

        int iter = 0;
        for (String root : LANGUAGE_PATHS){
            if (iter > 0){
                break;
            }

            for (String path : PRODUCT_PATHS){

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
                    // Check if already changed
                    if (child.getChild("jcr:content").getValueMap().get("cq:template", String.class).equals(TEMPLATE_PATH)){
                        continue;
                    }

                    // Create temporary new page to copy content onto
                    String pageName = "tempPage";
                    String newPagePath = root + path + "/" + pageName;
                    try {
                        page.create(root + path, pageName,
                                TEMPLATE_PATH, pageName);
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
            iter += 1;
        }
    }

    private void moveContent(Session session, String srcPath, String destPath){
        // Move Node
        try {
            session.removeItem(destPath);
            session.move(srcPath, destPath);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }



}
