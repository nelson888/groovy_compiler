package com.tambapps.analyzer

import groovy.transform.PackageScope

@PackageScope
final class TestsHolder {

  static final String[] EXPRESSION_TESTS = [
      '4 + 2',
      '25--4',
      '-43* (6 +45)',
      '5 ^ 4^2',
      '43 > 1',
      '43< 1',
      '43 >=1',
      '43<=1',
      '43== 1',
      '43 != !1',
      '!!! 1',
  ]


}
