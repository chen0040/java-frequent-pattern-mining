package com.github.chen0040.fpm.data;

import com.github.chen0040.fpm.utils.CollectionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by xschen on 8/2/2015.
 */
public class ItemSets {
   private List<ItemSet> sets = new ArrayList<>();

   public ItemSets addAll(List<ItemSet> fis) {
      sets.addAll(fis);
      return this;
   }


   public List<ItemSet> getSets() {
      return sets;
   }


   public void setSets(List<ItemSet> sets) {
      this.sets = sets;
   }

   public Stream<ItemSet> stream(){
      return sets.stream();
   }


   public int countSets() {
      return sets.size();
   }


   public ItemSet getItemSet(int i) {
      return sets.get(i);
   }


   public ItemSet removeItemSetAt(int i) {
      return sets.remove(i);
   }

   public Set<String> generateCombinations(){
      Set<String> result = new HashSet<>();
      for(int i=0; i < countSets(); ++i){
         ItemSet itemSet = getItemSet(i);
         List<String> items = itemSet.getItems();
         List<List<String>> combinations = CollectionUtil.generateCombinations(items);
         for(List<String> combination : combinations) {
            combination.sort(String::compareTo);
            result.add(combination.stream().collect(Collectors.joining(", ")));
         }
      }
      return result;
   }

   public boolean isSubsetOf(ItemSets rhs) {
      Set<String> allsets = generateCombinations();
      Set<String> rhssets = rhs.generateCombinations();
      for(String item : allsets) {
         if(!rhssets.contains(item)){
            return false;
         }
      }
      return true;
   }


   public String getSignature() {
      List<String> items = generateCombinations().stream().collect(Collectors.toList());
      items.sort(String::compareTo);
      return "(" + items.stream().collect(Collectors.joining(")\r\n(")) + ")";

   }
}
