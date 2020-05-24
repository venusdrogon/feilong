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
package com.feilong.excel;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.date.DateUtil.nowTimestamp;

import java.util.Map;

import com.feilong.core.lang.SystemUtil;
import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.test.AbstractTest;
import com.feilong.tools.slf4j.Slf4jUtil;

public abstract class AbstractWriteTest extends AbstractTest{

    protected static <T> void handle(String excelTemplateLocation,String sheetDefinitionLocation,Map<String, Object> data){
        handle(excelTemplateLocation, sheetDefinitionLocation, null, data);
    }

    protected static <T> void handlePerSheet(
                    String excelTemplateLocation,
                    String sheetDefinitionLocation,
                    String sheetName,
                    Map<String, Object> data){
        handle(excelTemplateLocation, sheetDefinitionLocation, toArray(sheetName), data);
    }

    protected static void handle(String excelTemplateLocation,String sheetDefinitionLocation,String[] sheetNames,Map<String, Object> beans){
        String outputFileName = Slf4jUtil.format(
                        SystemUtil.USER_HOME + "/feilong/excel/{}{}.{}",
                        sheetNames,
                        nowTimestamp(),
                        FilenameUtil.getExtension(excelTemplateLocation));

        ExcelWriteUtil.write(excelTemplateLocation, sheetDefinitionLocation, sheetNames, beans, outputFileName);

        DesktopUtil.open(outputFileName);
    }

}
