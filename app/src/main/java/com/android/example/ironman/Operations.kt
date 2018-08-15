package com.android.example.ironman

import java.math.BigDecimal
import java.math.MathContext

class Operations {
    companion object {


        @Throws(NumberFormatException::class)
        fun addValues(operationList: List<BigDecimal>): BigDecimal {
            val d1 = operationList[0]
            val d2 = operationList[1]
            return d2.add(d1)
        }

        @Throws(NumberFormatException::class)
        fun subtractValues(operationList: List<BigDecimal>): BigDecimal {
            val d1 = operationList[0]
            val d2 = operationList[1]
            return d2.subtract(d1)
        }

        @Throws(NumberFormatException::class)
        fun multiplyValues(operationList: List<BigDecimal>): BigDecimal {
            val d1 = operationList[0]
            val d2 = operationList[1]
            return d2.multiply(d1)
        }

        @Throws(NumberFormatException::class)
        fun divideValues(operationList: List<BigDecimal>): BigDecimal {
            val d1 = operationList[0]
            val d2 = operationList[1]
            var result: BigDecimal
            try {
                result = d2.divide(d1)
            } catch (e: ArithmeticException) {
                val mc = MathContext.DECIMAL128
                result = d2.divide(d1, mc)
            }

            return result
        }

        @Throws(NumberFormatException::class)
        fun exponentiateValues(operationList: List<BigDecimal>): BigDecimal {
            val d1 = operationList[0]
            val d2 = operationList[1]
            return BigDecimal(Math.pow(d2.toDouble(), d1.toDouble()))
            //        return BigDecimalMath.pow(d2,d1);
        }

        @Throws(NumberFormatException::class)
        fun calculateSin(d: BigDecimal): BigDecimal {
            return BigDecimal(Math.sin(d.toDouble()))
            //        return BigDecimalMath.sin(d);
        }

        @Throws(NumberFormatException::class)
        fun calculateCos(d: BigDecimal): BigDecimal {
            return BigDecimal(Math.cos(d.toDouble()))
            //        return BigDecimalMath.cos(d);
        }

        @Throws(NumberFormatException::class)
        fun calculateTan(d: BigDecimal): BigDecimal {
            return BigDecimal(Math.tan(d.toDouble()))
            //        return BigDecimalMath.tan(d);
        }

        @Throws(Exception::class)
        fun calculateASin(d: BigDecimal): BigDecimal {
            return if (d.abs().min(BigDecimal.ONE) === BigDecimal.ONE) {
                throw Exception("Error: cannot calculate arcsin($d)")
            } else {
                BigDecimal(Math.asin(d.toDouble()))
            }
            // return BigDecimalMath.asin(d);
        }

        @Throws(Exception::class)
        fun calculateACos(d: BigDecimal): BigDecimal {
            return if (d.abs().min(BigDecimal.ONE) === BigDecimal.ONE) {
                throw Exception("Error: cannot calculate arccos($d)")
            } else {
                BigDecimal(Math.acos(d.toDouble()))
            }
            //        return BigDecimalMath.acos(d);
        }

        @Throws(Exception::class)
        fun calculateATan(d: BigDecimal): BigDecimal {
            return BigDecimal(Math.atan(d.toDouble()))
            //        return BigDecimalMath.atan(d);
        }
    }
}