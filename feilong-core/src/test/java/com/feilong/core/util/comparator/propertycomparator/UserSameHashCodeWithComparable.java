/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.core.util.comparator.propertycomparator;

import com.feilong.lib.lang3.builder.CompareToBuilder;
import com.feilong.store.member.UserSameHashCode;

/**
 * for 极端测试
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.3
 */
public class UserSameHashCodeWithComparable extends UserSameHashCode implements Comparable<UserSameHashCodeWithComparable>{

    public UserSameHashCodeWithComparable(Integer id, String name){
        super(id, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(UserSameHashCodeWithComparable userSameHashCodeWithComparable){
        return CompareToBuilder.reflectionCompare(this, userSameHashCodeWithComparable);
    }

}
