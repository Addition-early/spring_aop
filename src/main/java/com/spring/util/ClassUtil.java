package com.spring.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载类
 */
public class ClassUtil {

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * 需要提供类名是否初始化标志
     * 初始化是指是否执行静态代码块
     *
     * @param className 类名
     * @param isInit 类是否需要被初始化
     * @return 加载后的类
     */
    public static Class<?> loadClass(String className, boolean isInit) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInit, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
        return cls;
    }

    /**
     * 加载类(默认将初始化类)
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }


    /**
     * 获得加载类的集合
     * @param packageName 扫描路径 com.spring.domain
     * @return 返回加载类的集合
     * @throws IOException IO读取异常
     */
    public static Set<Class<?>> getClassSet(String packageName) throws IOException {
        // 保存扫描到的类
        Set<Class<?>> classSet = new HashSet<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            // 遍历枚举
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();//获得URL的协议
                    System.out.println("protocol:" + protocol);
                    if (protocol.equals("file")) {
                        //获得转码后的绝对路径  packagePath:/Users/shenjiqu/IdeaProjects/spring_aop/target/classes/com/spring/domain
                        String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");//转码为utf-8的格式
                        System.out.println("packagePath:" + packagePath + "," + packageName);
                        addClass(classSet, packagePath, packageName);
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
        return classSet;
    }

    /**
     * 添加类到类集合中
     * @param classSet 保存类的集合
     * @param packagePath 扫描包的绝对路径 packagePath:/Users/shenjiqu/IdeaProjects/spring_aop/target/classes/com/spring/domain
     * @param packageName 扫描包的名字 com.spring.domain
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        //listFiles是获取该目录下所有文件和目录的绝对路径
        //FileFilter：过滤文件名
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                //获得的是标准文件且文件名称以class结尾 或者 是一个文件目录
                return (file.isFile() && file.getName().endsWith("class") || file.isDirectory());
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            System.out.println("fileName:" + fileName);
            // file是文件
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf(".")); // TestAspect
                System.out.println("className0:" + className);
                // 加上类的包名
                if (StringUtils.isNotBlank(packageName)) {
                    className = packageName + "." + className;  // com.spring.domain.TestAspect
                }
                System.out.println("className1:" + className);
                //添加 class
                doAddClass(classSet, className);
            }
            // file是目录
            else {
                //子目录
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                // 递归调用解析file
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 调用类加载的办法，添加类到集合中
     * @param classSet 存储类的集合
     * @param className 类名
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    public static void main(String[] args) {
        String s = "com/spring/test";
        try {
            System.out.println(getClassLoader().getResources(s).nextElement());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
