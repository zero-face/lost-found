package com.example.lostfound;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @Author Zero
 * @Date 2021/8/10 23:42
 * @Since 1.8
 * @Description
 **/
public class MyGenerator {
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir"); //当前项目路径
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("zero");
        gc.setOpen(false); //是否打开资源管理器
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setFileOverride(false);//是否覆盖
        gc.setServiceName("%sService");//去掉service的I前缀
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://82.157.191.65:3600/lost_found?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("chl252599");
        dsc.setDbType(DbType.MYSQL);//设置数据库为MySQL类型
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setModuleName("lostfound");
        pc.setParent("com.example");//设置模块父路径
        pc.setEntity("entity");
        pc.setController("controller");

        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("impl");
        mpg.setPackageInfo(pc);

        //策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("t_found_commont","t_found_thing","t_loss_commont","t_loss_thing","t_permission","t_user","t_user_permission");
        strategy.setNaming(NamingStrategy.underline_to_camel);//包开启驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel); //字段开启驼峰命名
        strategy.setEntityLombokModel(true);//自动了lombok
        strategy.setLogicDeleteFieldName("deleted");//设置逻辑删除
        //设置自动填充
        TableFill gmtCreate = new TableFill("last_login_time", FieldFill.INSERT);//插入填充
        final ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        strategy.setTableFillList(tableFills);
        //乐观锁
        //strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);//controller层设置rest风格
        strategy.setControllerMappingHyphenStyle(true);//开启controller访问下划线命名s
        mpg.setStrategy(strategy);
        mpg.execute();
    }


}
