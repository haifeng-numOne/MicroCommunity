package com.java110.web.smo.org.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.org.IDeleteOrgSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



/**
 * 添加小区服务实现类
 * delete by wuxw 2019-06-30
 */
@Service("deleteOrgSMOImpl")
public class DeleteOrgSMOImpl extends AbstractComponentSMO implements IDeleteOrgSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        //super.validatePageInfo(pd);

        //Assert.hasKeyAndValue(paramIn, "xxx", "xxx");
        Assert.hasKeyAndValue(paramIn, "orgId", "组织ID不能为空");


        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.LIST_ORG);

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ResponseEntity<String> responseEntity = null;
        //super.validateStoreStaffCommunityRelationship(pd, restTemplate);
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        paramIn.put("storeId", result.getStoreId());
        responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/org.deleteOrg",
                HttpMethod.POST);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteOrg(IPageData pd) {
        return super.businessProcess(pd);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
