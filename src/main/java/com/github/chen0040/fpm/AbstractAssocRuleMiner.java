package com.github.chen0040.fpm;

import com.github.chen0040.fpm.data.ItemSet;
import com.github.chen0040.fpm.data.ItemSets;

import java.util.List;


/**
 * Created by xschen on 8/2/2015.
 */
public abstract  class AbstractAssocRuleMiner implements AssocRuleMiner {

   private int minSupportLevel;


   public int getMinSupportLevel() {
      return minSupportLevel;
   }


   public void setMinSupportLevel(int minSupportLevel) {
      this.minSupportLevel = minSupportLevel;
   }


   public abstract ItemSets minePatterns(Iterable<? extends List<String>> database, List<String> uniqueItems);

   public ItemSets findMaxPatterns(Iterable<? extends List<String>> database, List<String> uniqueItems)
   {
      return findMaxPatterns(minePatterns(database, uniqueItems));
   }

   protected ItemSets findMaxPatterns(ItemSets fis)
   {
      int count = fis.countSets();
      for (int i = count - 1; i >= 0; i--)
      {
         ItemSet itemset = fis.getItemSet(i);
         boolean isSubSet = false;
         for (int j = 0; j < fis.countSets(); j++)
         {
            if (i == j) continue;
            isSubSet = true;

            ItemSet itemset2 = fis.getItemSet(j);
            if (itemset.countItems() > itemset2.countItems())
            {
               isSubSet = false;
            } else {
               for (int k = 0; k < itemset.countItems(); ++k) {
                  if (!itemset2.containsItem(itemset.getItemAt(k))) {
                     isSubSet = false;
                     break;
                  }
               }
            }

            if (isSubSet) break;
         }

         if (isSubSet)
         {
            fis.removeItemSetAt(i);
         }
      }
      return fis;
   }
}
