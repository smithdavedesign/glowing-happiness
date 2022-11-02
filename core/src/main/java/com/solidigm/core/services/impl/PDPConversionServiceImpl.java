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

    private static String ROOT_PATH = "/content/solidigm/language-masters/en";

    private static String TEMPLATE_PATH = "/conf/solidigm/settings/wcm/templates/product-detail-page--pdp-1";

    private static String TOP_CONTAINER = "container_2089249899";

    private static ArrayList<String> PRODUCT_PATHS = new ArrayList<>(Arrays.asList(
            "/products/data-center/d7", "/products/data-center/d5", "/products/data-center/d3",
            "/products/client/d6/p44/P44p-series", "/products/client/d6/p31/P31p-series",
            "/products/client/d6/p41/p41p-series", "/products/client/d6/670p/670p-series"
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

        // Get page resource
        Resource resource = resolver.getResource(ROOT_PATH + PRODUCT_PATHS.get(0));
        PageManager page = resolver.adaptTo(PageManager.class);
        Session session = resolver.adaptTo(Session.class);
        //Workspace workspace = resolver.adaptTo(Session.class).getWorkspace();

        // Iterate through children pages
        int iter = 0;
        for (Resource child : resource.getChildren()){
            if (iter > 0){
                break;
            }
            // Skip parent's jcr:content
            if (child.getName().equals("jcr:content")){
                continue;
            }

            // Create new page
            String pageName = "test" + Integer.toString(iter);
            String newPagePath = ROOT_PATH + PRODUCT_PATHS.get(0) + "/" + pageName;
            try {
                page.create(ROOT_PATH + PRODUCT_PATHS.get(0), pageName,
                        TEMPLATE_PATH, pageName);
            } catch (WCMException e) {
                throw new RuntimeException(e);
            }

            Resource newPage = resolver.getResource(newPagePath);

            // Get destination paths for cloning
            String topDestPath = newPagePath + "/jcr:content/root/container_2089249899/";
            String bottomDestPath = newPagePath + "/jcr:content/root/container/";

            // Iterate through source page's content
            for (Resource parentContent : child.getChild("jcr:content/root").getChildren()){
                // Skip header/footer
                if (parentContent.getName().equals("header") || parentContent.getName().equals("footer")){
                    continue;
                }

                // Decide if it goes in top container or bottom container
                String sourcePath = parentContent.getPath();
                if (parentContent.getName().equals("productphotogallery")
                        || parentContent.getName().equals("productoverview")){
                    // Move node
                    try {
                        session.removeItem(topDestPath + parentContent.getName());
                        session.move(parentContent.getPath(), topDestPath + parentContent.getName());
                    } catch (RepositoryException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    // Move Node
                    try {
                        session.removeItem(bottomDestPath + parentContent.getName());
                        session.move(parentContent.getPath(), bottomDestPath + parentContent.getName());
                    } catch (RepositoryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            ValueMap sourceMap = child.getChild("jcr:content/root").getValueMap();

            Node destRoot = newPage.getChild("jcr:content/root").adaptTo(Node.class);
            Node sourcePage = child.getChild("jcr:content").adaptTo(Node.class);
            try {
                destRoot.setProperty("jcr:mixinTypes", sourceMap.get("jcr:mixinTypes", String.class));
                sourcePage.setProperty("cq:template", "/conf/solidigm/settings/wcm/templates/product-detail-page--pdp-1");
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }

            // Move updated root back, delete temporary page
            try {
                session.removeItem(child.getPath() + "/jcr:content/root");
                session.move(newPagePath + "/jcr:content/root", child.getPath() + "/jcr:content/root");
                session.removeItem(newPagePath);
                session.save();
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
            iter += 1;
        }
    }

}
