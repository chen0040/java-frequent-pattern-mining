package com.github.chen0040.fpm.data;


import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by xschen on 8/2/2015.
 */
@Getter
public class MetaData {

   private List<String> uniqueItems;
   private int dbSize;

   public MetaData(Iterable<? extends List<String>> database){
      Set<String> set = new HashSet<>();
      dbSize = 0;
      for(List<String> transaction : database){
         set.addAll(transaction);
         dbSize++;
      }
      uniqueItems = set.stream().collect(Collectors.toList());
   }
}
