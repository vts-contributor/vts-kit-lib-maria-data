/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.vtskit.maria.repository;

import com.viettel.vtskit.maria.utils.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CommonMariaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private Query createNativeSelectQuery(String query){
        return entityManager.createNativeQuery(query, Tuple.class);
    }

    private Query createNativeQuery(String query){
        return entityManager.createNativeQuery(query);
    }

    private String removeOrderBy(String strReplace) {
        String strResult = strReplace.toLowerCase();
        int indexLast = strResult.lastIndexOf("order by");
        int indexLastCm = strResult.lastIndexOf(")");
        if (indexLast > 0 && indexLastCm < indexLast) {
            strResult = strResult.substring(0, indexLast);
        }
        return strResult;
    }

    public <T> List<T> executeSelectQuery(String query, Map<String, Object> params, Class<T> resultClass){
        Query nativeQuery = createNativeSelectQuery(query);
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, Object> param : params.entrySet()){
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        List<T> resultList = CommonUtils.convertToEntity(nativeQuery.getResultList(), resultClass);
        entityManager.close();
        return resultList;
    }

    public long executeCountSelectQuery(String query, Map<String, Object> params){
        StringBuilder strBuild = new StringBuilder();
        String strSql = removeOrderBy(query);
        strBuild.append("Select count(1) as count From (");
        strBuild.append(strSql);
        strBuild.append(") tbcount");
        Query nativeQuery = createNativeQuery(strBuild.toString());
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, Object> param : params.entrySet()){
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        List resultQr = nativeQuery.getResultList();
        entityManager.close();
        if(resultQr == null || resultQr.isEmpty()){
            return 0;
        }
        return Long.parseLong(String.valueOf(resultQr.get(0)));
    }

    public <T> Page<T> executeSelectPagingQuery(String query, Map<String, Object> params, int startPage,
                                                int pageSize, Class<T> resultClass){
        Query nativeQuery = createNativeSelectQuery(query);
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, Object> param : params.entrySet()){
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        nativeQuery.setFirstResult(startPage).setMaxResults(startPage + pageSize);
        long total = executeCountSelectQuery(query, params);
        List<T> resultList = CommonUtils.convertToEntity(nativeQuery.getResultList(), resultClass);
        Page<T> result = new PageImpl<T>(resultList, PageRequest.of(startPage, pageSize), total);
        entityManager.close();
        return result;
    }

    public int executeUpdateQuery(String query, Map<String, Object> params){
        Query nativeQuery = createNativeQuery(query);
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, Object> param : params.entrySet()){
                nativeQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        int resultUpdate = nativeQuery.executeUpdate();
        entityManager.close();
        return resultUpdate;
    }

}
