package dev.trier.ecommerce.utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {


    public static void copyNonNullProperties(Object source, Object target, String... ignoreProperties){

        Set<String> ignoreList = new HashSet<>(Arrays.asList(ignoreProperties));
        ignoreList.addAll(Arrays.asList(getNullPropertyNames(source)));

        BeanUtils.copyProperties(source, target, ignoreList.toArray(new String[0]));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds =  src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            if  (srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}