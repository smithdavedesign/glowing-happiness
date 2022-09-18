package com.solidigm.core.services.impl;

import com.solidigm.core.beans.ProductModel;
import com.solidigm.core.beans.ProductVariation;
import com.solidigm.core.services.ProductService;
import com.solidigm.core.services.ProductServiceConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, service = ProductService.class)
@Designate(ocd = ProductServiceConfig.class)
public class ProductServiceImpl implements ProductService {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private String productDataPath;

    private Map<String, ProductModel> products;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private ResourceResolver resourceResolver;

    @Activate
    public void activate(ProductServiceConfig config) {
        productDataPath = config.getProductDataPath();
        products = new HashMap<>();
    }

    //Returns product data from the JCR at the requested path
    private void resolveProduct(String path) {
        //Log in user and get resource resolver if it does not exist
        if (resourceResolver == null) {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "productServiceUser");
            try {
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            } catch (LoginException e) {
                log.error("Failed to log in Service User", e);
                return;
            }
        }

        //Get product data from the JCR at the requested path
        Resource productResource = resourceResolver.getResource(path);
        if (productResource != null) {
            products.put(path, productResource.adaptTo(ProductModel.class));
        }
    }

    //Build path to product data based on configured root path and provided arguments
    private String buildPath(String category, String series, String name) {
        String path = "";
        if (StringUtils.isNotEmpty(category)) {
            path = path.concat("/").concat(category);
        }
        if (StringUtils.isNotEmpty(series)) {
            path = path.concat("/").concat(series);
        }
        if (StringUtils.isNotEmpty(name)) {
            path = path.concat("/").concat(name);
        }
        return productDataPath.concat(path);
    }

    @Override
    public void reset() {
        products = new HashMap<>();
    }

    @Override
    public ProductModel getProduct(String category, String series, String name) {
        String path = buildPath(category, series, name);
        if (!products.containsKey(path)) {
            resolveProduct(path);
        }
        return products.get(path);
    }
}
