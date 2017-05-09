package com.github.chen0040.fpm;


import com.github.chen0040.fpm.data.ItemSets;

import java.util.List;


/**
 * Created by xschen on 8/2/2015.
 */
public interface AssocRuleMiner {
   int getMinSupportLevel();
   void setMinSupportLevel(int level);
   ItemSets findMaxPatterns(Iterable<? extends List<String>> database, List<String> uniqueItems);
   ItemSets minePatterns(Iterable<? extends List<String>> database, List<String> uniqueItems);
}
