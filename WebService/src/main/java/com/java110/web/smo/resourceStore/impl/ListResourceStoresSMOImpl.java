package com.java110.web.smo.resourceStore.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.resourceStore.IListResourceStoresSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询resourceStore服务类
 */
@Service("listResourceStoresSMOImpl")
public class ListResourceStoresSMOImpl extends AbstractComponentSMO implements IListResourceStoresSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listResourceStores(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.AGENT_HAS_LIST_RESOURCESTORE);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);
        paramIn.put("storeId",result.getStoreId());

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/resourceStore.listResourceStores" + mapToUrlParam(paramIn);


        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
