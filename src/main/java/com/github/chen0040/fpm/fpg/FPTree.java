package com.github.chen0040.fpm.fpg;

import com.github.chen0040.data.utils.StringUtils;
import com.github.chen0040.data.utils.TupleTwo;
import com.github.chen0040.fpm.data.ItemSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by xschen on 8/2/2015.
 */
public class FPTree {
   private FPTreeNode root = new FPTreeNode();
   private Map<String, Integer> frequency = new HashMap<>();
   private Map<String, List<FPTreeNode>> heads = new HashMap<>();
   private static final Logger logger = LoggerFactory.getLogger(FPTree.class);
   private String base;
   private FPTree parent;
   private boolean debugMode = false;

   public FPTree(){

   }

   public void setDebugMode(boolean debugMode) {
      this.debugMode = debugMode;
   }

   public FPTree(FPTree parent, String base){
      this.parent = parent;
      this.base = base;
   }

   public List<String> getBaseList(){
      FPTree tree = this;
      List<String> result = new ArrayList<>();
      while(tree != null){
         if(!StringUtils.isEmpty(tree.base)){
            result.add(tree.base);
         }

         tree = tree.parent;
      }
      Collections.reverse(result);

      return result;
   }


   public void addOrderedFreqItems(List<TupleTwo<String, Integer>> orderedFreqItems) {

      append(root, orderedFreqItems, 0);
   }

   protected void append(FPTreeNode node, List<TupleTwo<String, Integer>> orderedFreqItems, int d)
   {
      String currentItem = orderedFreqItems.get(d)._1();
      int itemCount = orderedFreqItems.get(d)._2();

      int selectedIndex = -1;
      for (int i = 0; i < node.childCount(); ++i)
      {
         if (node.child(i).getItem().equals(currentItem))
         {
            selectedIndex = i;
            break;
         }
      }

      if (selectedIndex != -1)
      {
         node.child(selectedIndex).incCount(itemCount);

         if (d < orderedFreqItems.size() - 1)
         {
            append(node.child(selectedIndex), orderedFreqItems, d + 1);
         }
      }
      else
      {
         FPTreeNode child = new FPTreeNode(currentItem, itemCount);
         node.addChild(child);

         if(heads.containsKey(currentItem)){
            heads.get(currentItem).add(child);
         } else {
            List<FPTreeNode> nodes = new ArrayList<>();
            nodes.add(child);
            heads.put(currentItem, nodes);
         }

         if (d < orderedFreqItems.size() - 1)
         {
            append(child, orderedFreqItems, d + 1);
         }
      }
   }

   public void constructTree(List<ItemSet> conditionalPatternBase, int minSupport) {

      frequency = new HashMap<>();
      for (ItemSet transaction : conditionalPatternBase) {
         Set<String> uniqueItems = new HashSet<>(transaction.getItems());
         for (String item : uniqueItems) {
            frequency.put(item, frequency.getOrDefault(item, 0) + transaction.getSupport());
         }
      }

      List<TupleTwo<String, Integer>> freqItems = frequency.entrySet()
              .stream()
              .map(entry -> new TupleTwo<>(entry.getKey(), entry.getValue()))
              .collect(Collectors.toList());

      freqItems.sort((a, b) -> -Integer.compare(a._2(), b._2()));



      for(ItemSet transaction : conditionalPatternBase) {
         List<TupleTwo<String, Integer>> orderedFreqItems = new ArrayList<>();
         for (int i = 0; i < freqItems.size(); ++i) {
            String item = freqItems.get(i)._1();
            if (transaction.containsItem(item)) {
               orderedFreqItems.add(new TupleTwo<>(item, transaction.getSupport()));
            }
         }
         if (orderedFreqItems.isEmpty()) {
            continue;
         }
         addOrderedFreqItems(orderedFreqItems);
      }

      if(debugMode) {
         print();
      }
   }

   public void print(){
      root.print(getBaseList().stream().collect(Collectors.joining(", ")));
   }

   public void mineTree(Set<ItemSet> result, int minSupportLevel) {
      heads.entrySet().stream()
              .forEach(entry -> {
                 String item = entry.getKey();
                 List<FPTreeNode> headNodes = entry.getValue();
                 List<FPPath> paths = headNodes.stream().map(FPTreeNode::getPath).collect(Collectors.toList());
                 List<ItemSet> conditionalPatternBase = new ArrayList<>();
                 for(int i=0; i < paths.size(); ++i) {

                    FPPath path = paths.get(i);

                    if (path.isEmpty()){
                       continue;
                    }

                    boolean singlePath = true;

                    if(path.size() > 1) {
                       for (int j = 0; j < path.size(); ++j) {
                          if (path.get(j).childCount() > 1) {
                             singlePath = false;
                          }
                       }
                    }

                    if (singlePath) {
                       ItemSet itemSet = new ItemSet();
                       itemSet.addAll(getBaseList());
                       itemSet.addAll(path.stream().map(FPTreeNode::getItem).collect(Collectors.toList()));
                       itemSet.setSupport(path.get(0).getCount());
                       if(itemSet.getSupport() >= minSupportLevel){
                          result.add(itemSet);
                       }
                    }

                    int currentItemCount = path.get(0).getCount();
                    ItemSet itemSet = new ItemSet();
                    for (int j = 1; j < path.size(); ++j) {
                       itemSet.addItem(path.get(j).getItem());
                    }
                    itemSet.setSupport(currentItemCount);

                    conditionalPatternBase.add(itemSet);
                 }

                 if(!conditionalPatternBase.isEmpty()) {
                    FPTree conditionalTree = new FPTree(this, item);
                    conditionalTree.constructTree(conditionalPatternBase, minSupportLevel);
                    conditionalTree.mineTree(result, minSupportLevel);
                 }
              });
   }

   public List<ItemSet> mineTree(int minSupport){
      Set<ItemSet> result = new HashSet<>();
      mineTree(result, minSupport);
      return result.stream().collect(Collectors.toList());
   }

   public void constructTree(Iterable<? extends List<String>> database, int minSupportLevel) {


      frequency = new HashMap<>();
      for (List<String> transaction : database) {
         Set<String> uniqueItems = new HashSet<>(transaction);
         for (String item : uniqueItems) {
            frequency.put(item, frequency.getOrDefault(item, 0) + 1);
         }
      }

      List<TupleTwo<String, Integer>> freqItems = frequency.entrySet()
              .stream()
              .filter(entry -> entry.getValue() >= minSupportLevel)
              .map(entry -> new TupleTwo<>(entry.getKey(), entry.getValue()))
              .collect(Collectors.toList());

      freqItems.sort((a, b) -> -Integer.compare(a._2(), b._2()));



      for(List<String> transaction : database) {
         List<TupleTwo<String, Integer>> orderedFreqItems = new ArrayList<>();
         for (int i = 0; i < freqItems.size(); ++i) {
            String item = freqItems.get(i)._1();
            if (transaction.contains(item)) {
               orderedFreqItems.add(new TupleTwo<>(item, 1));
            }
         }

         if (orderedFreqItems.isEmpty()) {
            continue;
         }
         addOrderedFreqItems(orderedFreqItems);
      }

      if(debugMode) {
         print();
      }


   }
}
