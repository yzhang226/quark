package org.lightning.quark.db.test;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/2/27
 */
public class SimpleTest {

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("a", "b", "c", "d");

        List<String> list1 = list.stream().map(x -> x+"1").collect(Collectors.toList());

        System.out.println(list1);

        list1.add("f");

        System.out.println(list1);


        System.out.println(Objects.hash(list));
        System.out.println(Objects.hash(list.toArray()));

        /**
         * int result = 1;

         for (Object element : a)
         result = 31 * result + (element == null ? 0 : element.hashCode());

         return result;
         */

        /**
         * public int hashCode() {
         int hashCode = 1;
         for (E e : this)
         hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
         return hashCode;
         }
         */

    }

}
