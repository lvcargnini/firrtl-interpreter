// See LICENSE for license details.

package firrtl_interpreter

import org.scalatest.{Matchers, FreeSpec}

class RegisterCycleTest extends FreeSpec with Matchers {
  "cycle behavtest-only firior should not crash on various register init methods" - {
    "method 1" in {
      val input =
        """
          |circuit myModule :
          |  module mySubModule :
          |    input clock : Clock
          |    input reset : UInt<1>
          |    input io_In : UInt<8>
          |    output io_Out : UInt<8>
          |
          |    reg delayReg : UInt<8>, clock with :
          |      reset => (reset, UInt<8>("h0"))
          |    io_Out <= delayReg
          |    delayReg <= io_In
          |
          |  module myModule :
          |    input clock : Clock
          |    input reset : UInt<1>
          |    input io_In : UInt<8>
          |    output io_Out : UInt<8>
          |
          |    inst mySubModule_1 of mySubModule @[myModuleTest.scala 22:25]
          |    io_Out <= mySubModule_1.io_Out
          |    mySubModule_1.io_In <= io_In
          |    mySubModule_1.clock <= clock
          |    mySubModule_1.reset <= reset
          |
        """.stripMargin


      for(i <- 0 to 10) {
        println(s"experiment $i")
        scala.util.Random.setSeed(i.toLong)
        val tester = new InterpretiveTester(input)
//        tester.setVerbose(true)

        tester.poke("reset", 1)
        tester.step(1)
        tester.poke("io_In", 1)
        tester.poke("reset", 0)
        tester.step(1)
        tester.expect("io_Out", 1)
      }
    }

    "method 2" in {
      val input =
        """
          |circuit myModule :
          |  module mySubModule :
          |    input clock : Clock
          |    input reset : UInt<1>
          |    input io_In : UInt<8>
          |    output io_Out : UInt<8>
          |
          |    reg delayReg : UInt<8>, clock
          |    io_Out <= delayReg
          |    delayReg <= io_In
          |
          |  module myModule :
          |    input clock : Clock
          |    input reset : UInt<1>
          |    input io_In : UInt<8>
          |    output io_Out : UInt<8>
          |
          |    inst mySubModule_1 of mySubModule @[myModuleTest.scala 22:25]
          |    io_Out <= mySubModule_1.io_Out
          |    mySubModule_1.io_In <= io_In
          |    mySubModule_1.clock <= clock
          |    mySubModule_1.reset <= reset
          |
        """.stripMargin


      val tester = new InterpretiveTester(input)
      tester.setVerbose(true)

      tester.poke("io_In", 1)
      tester.step(3)
      tester.expect("io_Out", 1)
    }
  }

}
