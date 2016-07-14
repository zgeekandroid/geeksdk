package com.commonslibrary.commons.net;

import java.util.Map;

/**
 * date        :  2015-11-30  11:11
 * author      :  Mickaecle gizthon
 * description :  local request interface
 */
public interface IRequestLocal    {
    /**
     * request local cache data
     * @param parameters  result back extends BaseDomain
     */
   <T> T doQueryLocal(Map<String, String> parameters, Class<T> cls);
}
