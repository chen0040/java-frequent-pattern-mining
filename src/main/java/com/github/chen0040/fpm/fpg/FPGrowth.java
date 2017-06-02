package com.github.chen0040.fpm.fpg;


import com.github.chen0040.fpm.AbstractAssocRuleMiner;
import com.github.chen0040.fpm.data.ItemSet;
import com.github.chen0040.fpm.data.ItemSets;

import java.util.List;


/**
 * Created by xschen on 8/2/2015.
 */
public class FPGrowth extends AbstractAssocRuleMiner {

   private FPTree fpTree;

   @Override
   public ItemSets minePatterns(Iterable<? extends List<String>> database, List<String> uniqueItems)
   {
      fpTree = new FPTree();
      fpTree.constructTree(database, getMinSupportLevel());
      List<ItemSet> result = fpTree.mineTree(getMinSupportLevel());
      return new ItemSets().addAll(result);
   }

   public FPTree tree(){
      return fpTree;
   }


}
