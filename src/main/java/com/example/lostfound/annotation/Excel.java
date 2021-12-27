package com.example.lostfound.annotation;

/**
 * @author Zero
 * @date 2021/12/25 23:21
 * @description
 * @since 1.8
 **/
public @interface Excel {

    /**
     * 导出时在Excel中排序
     */
    int sort() default Integer.MAX_VALUE;

    /**
     * 导出到Excel中的名字
     */
    String name() default "";

    /**
     * 日期格式
     */
    String dateFormat() default "";

    /**
     * 如果是字典类型，请设置字典的type值 (如: sys_user_sex)
     */
    String dictType() default "";
}
