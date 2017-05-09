package com.github.chen0040.fpm.apriori;

import com.github.chen0040.fpm.AbstractAssocRuleMiner;
import com.github.chen0040.fpm.data.ItemSet;
import com.github.chen0040.fpm.data.ItemSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class Apriori extends AbstractAssocRuleMiner {

    public ItemSets minePatterns(Iterable<? extends List<String>> database, List<String> uniqueItems) {

        ItemSets warehouse = new ItemSets();

        List<ItemSet> frequent_itemset_cluster = scan4OneItemFrequentItemSets(database, uniqueItems);
        warehouse.addAll(frequent_itemset_cluster);

        int k = 1;

        while(frequent_itemset_cluster.size() > 0){

            List<ItemSet> C = new ArrayList<>();
            for(int i=0; i < frequent_itemset_cluster.size(); ++i){
                ItemSet frequent_itemset_i = frequent_itemset_cluster.get(i);
                for(int j =0; j < frequent_itemset_cluster.size(); ++j){
                    if(i==j) continue;

                    ItemSet frequent_itemset_j = frequent_itemset_cluster.get(j);

                    boolean canJoin = true;
                    for(int l=0; l < k-1; ++l){
                        if(!frequent_itemset_i.getItemAt(l).equals(frequent_itemset_j.getItemAt(l))){
                            canJoin = false;
                            break;
                        }
                    }

                    if(canJoin){
                        if(frequent_itemset_i.getItemAt(k-1).compareTo(frequent_itemset_j.getItemAt(k-1)) > 0){
                            canJoin = false;
                        }
                    }

                    if(canJoin){
                        ItemSet candidate = frequent_itemset_i.makeCopy();
                        candidate.setParentSupport(frequent_itemset_i.getSupport());
                        candidate.addItem(frequent_itemset_j.lastItem());
                        C.add(candidate);
                    }
                }
            }

            updateItemSupport(database, C, uniqueItems);

            frequent_itemset_cluster = getFrequentItemSets(C, k+1, uniqueItems);

            if(frequent_itemset_cluster.size() > 0) {
                warehouse.addAll(frequent_itemset_cluster);
            }
            k++;
        }

        return warehouse;
    }

   protected void updateItemSupport(Iterable<? extends List<String>> database, List<ItemSet> C, List<String> uniqueItems){
      for(int j=0; j < C.size(); ++j){
         C.get(j).setSupport(0);
      }

      for(List<String> transaction : database){

         for(int j=0; j < C.size(); ++j){
            ItemSet itemset = C.get(j);
            if(containsItems(transaction, itemset)){
               itemset.incSupport();
            }
         }
      }
   }


   private boolean containsItems(List<String> transaction, ItemSet itemset) {
      for(String item : itemset.getItems()){
         if(!transaction.contains(item)){
            return false;
         }
      }
      return true;
   }


   protected List<ItemSet> getFrequentItemSets(List<ItemSet> C, int k, List<String> uniqueItems){
        List<ItemSet> frequent_itemset_cluster = new ArrayList<>();
        for(ItemSet itemset : C){

            if(itemset.getSupport() >= getMinSupportLevel()){
                frequent_itemset_cluster.add(itemset);
            }
        }
        return frequent_itemset_cluster;
    }

    private List<ItemSet> scan4OneItemFrequentItemSets(Iterable<? extends List<String>> database, List<String> uniqueItems) {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < uniqueItems.size(); ++i)
        {
            counts.put(uniqueItems.get(i), 0);
        }

        for (List<String> transaction : database)
        {
            for (int i = 0; i < uniqueItems.size(); ++i)
            {
                String item = uniqueItems.get(i);
                if (transaction.contains(item))
                {
                    counts.put(item, counts.get(item)+1);
                }
            }
        }

        List<ItemSet> result = new ArrayList<>();

        for(String item : uniqueItems){
            int count = counts.get(item);
            if(count >= getMinSupportLevel()){
                ItemSet itemSet = new ItemSet();
                itemSet.addItem(item);
                itemSet.setSupport(count);

                result.add(itemSet);
            }
        }

       return result;
    }

}
