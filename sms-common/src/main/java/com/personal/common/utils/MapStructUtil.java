package com.personal.common.utils;

import com.personal.common.model.StandardReport;
import com.personal.common.model.StandardSubmit;
import org.mapstruct.Mapper;

/**
 * @ClassName MapStructUtil
 * @Author liupanpan
 * @Date 2026/1/16
 * @Description 对象映射工具
 * 设置componentModel = "spring"后，不需要INSTANCE了，可以通过@Autowired注入
 * MapStructUtils INSTANCE = Mappers.getMapper(MapStructUtils.class);
 *  * 1. 默认情况下同名字段会自动映射，无需额外配置，如果有其他不同名的字段，可以继续使用 @Mapping 注解
 *  * 2. 支持多个源对象的属性映射到一个目标对象
 *  * 3. 条件映射
 */
@Mapper(componentModel = "spring")
public interface MapStructUtil {
    StandardReport getStandardReport(StandardSubmit standardSubmit);
}
