package com.github.chen0040.fpm.utils;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 9/5/2017.
 */
public class CollectionUtil {

   public static List<List<String>> generateCombinations(List<String> items) {
      List<List<String>> combination = new ArrayList<>();

      List<List<String>> sets = new ArrayList<>();

      for(int i=0; i < items.size(); ++i){
         List<String> set = new ArrayList<>();
         set.add(items.get(i));
         sets.add(set);
      }
      combination.addAll(sets);

      while(!sets.isEmpty()){
         List<List<String>> newSets = new ArrayList<>();

         for(int j=0; j < sets.size(); ++j){
            List<String> setj = sets.get(j);
            for(int k=0; k < sets.size(); ++k) {
               if(j == k) continue;

               List<String> setk = sets.get(k);

               boolean shouldCombine = true;
               for(int l=0; l < setj.size()-1; ++l){
                  if(!setj.get(l).equals(setk.get(l))){
                     shouldCombine = false;
                     break;
                  }
               }

               if(shouldCombine && setj.get(setj.size()-1).compareTo(setk.get(setk.size()-1)) < 0){
                  List<String> setm = new ArrayList<>();
                  setm.addAll(setj);
                  setm.add(setk.get(setk.size()-1));
                  newSets.add(setm);
               }
            }
         }

         if(!newSets.isEmpty()) {
            combination.addAll(newSets);
         }
         sets = newSets;
      }

      return combination;

   }

}
