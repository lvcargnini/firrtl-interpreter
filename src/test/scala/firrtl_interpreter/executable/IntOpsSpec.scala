// See LICENSE for license details.

package firrtl_interpreter.executable

import org.scalatest.{FreeSpec, Matchers}


// scalastyle:off magic.number
class IntOpsSpec extends FreeSpec with Matchers {
  def f0(): Int = 0
  def f1(): Int = 1
  def f2(): Int = 2
  def f3(): Int = 3
  def fMinus1(): Int = -1
  def fMinus2(): Int = -2
  def fMinus3(): Int = -3
  def fMinus4(): Int = -4
  def fMinus6(): Int = -6

  def val1(): Int = Integer.parseInt("abcd", 16)
  def val2(): Int = Integer.parseInt("10" * 4, 2)
  def val3(): Int = Integer.parseInt("0", 2)

  "IntOps should pass a basic test" - {
    "AsUIntInts should work" in {
      AsUIntInts(fMinus6, width = 4)() should be (10)
      AsUIntInts(() => -22, width = 16)() should be (65514)
      AsUIntInts(fMinus4, width = 4)() should be (12)

      AsUIntInts(f0, width = 1)() should be (0)
      AsUIntInts(fMinus1, width = 1)() should be (1)
      AsUIntInts(f0, width = 1)() should be (0)
      AsUIntInts(f1, width = 1)() should be (1)

      AsUIntInts(f3, width = 3)() should be (3)
      AsUIntInts(fMinus4, width = 3)() should be (4)
    }

    "AsSIntInts should work" in {
      AsSIntInts(f0, width = 1)() should be (0)
      AsSIntInts(fMinus1, width = 1)() should be (-1)
      AsSIntInts(f0, width = 1)() should be (0)
      AsSIntInts(f1, width = 1)() should be (-1)

      AsSIntInts(f3, width = 2)() should be (-1)
      AsSIntInts(f3, width = 3)() should be (3)
    }

    "cat ops should combine bits from two numbers" in {
      CatInts(f1, f1IsSigned = false, f1Width = 1, f1, f2IsSigned = false, f2Width = 1)() should be (3)
      CatInts(fMinus1, f1IsSigned = true,  f1Width = 2, f1, f2IsSigned = false, f2Width = 1)() should be (7)

      CatInts(fMinus1, f1IsSigned = true,  f1Width = 2, fMinus1, f2IsSigned = true, f2Width = 2)() should be (15)
      CatInts(fMinus1, f1IsSigned = true,  f1Width = 2, fMinus2, f2IsSigned = true, f2Width = 2)() should be (14)
      CatInts(f1, f1IsSigned = false,  f1Width = 1, fMinus1, f2IsSigned = true, f2Width = 2)() should be (7)
    }

    "bit ops should take arbitrary bits from a value" in {
      BitsInts(val2, high = 1, low = 0, originalWidth = 8)() should be (2)
      BitsInts(val2, high = 2, low = 0, originalWidth = 8)() should be (2)
      BitsInts(val2, high = 3, low = 0, originalWidth = 8)() should be (10)
      BitsInts(val2, high = 3, low = 1, originalWidth = 8)() should be (5)
      BitsInts(val2, high = 3, low = 2, originalWidth = 8)() should be (2)
      BitsInts(val2, high = 3, low = 3, originalWidth = 8)() should be (1)
    }
  }
}
