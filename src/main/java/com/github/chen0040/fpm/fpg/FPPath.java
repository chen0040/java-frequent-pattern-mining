package com.github.chen0040.fpm.fpg;


import java.util.ArrayList;


/**
 * Created by xschen on 8/2/2015.
 */
public class FPPath extends ArrayList<FPTreeNode> {
   private boolean singlePrefix = false;


   public boolean isSinglePrefix() {
      return singlePrefix;
   }


   public void setSinglePrefix(boolean singlePrefix) {
      this.singlePrefix = singlePrefix;
   }
}
