package com.android.example.ironman

import android.content.Context
import java.math.BigDecimal
import java.math.MathContext
import java.util.*
import java.util.regex.Pattern

class Maths {

    companion object {


        @Throws(Exception::class)
        fun doMath(input: String, c: Context): String? {

            // Check for parenthesis errors before doing anything else:
            checkParen(input, c)
            val postfix = convertToPostfix(input, c)
            return evaluatePostfix(postfix, c)
        }

        @Throws(Exception::class)
        private fun checkParen(input: String, c: Context) {
            var parenCounter = 0
            var unbalanced = false
            for (ch in input.toCharArray()) {
                when (ch) {
                    '(' -> parenCounter++
                    ')' -> parenCounter--
                }
                if (parenCounter == -1) {
                    unbalanced = true
                }
            }

            if (parenCounter < 0) {
                throw Exception(c.getString(R.string.paren_r_over_l))
            } else if (parenCounter > 0) {
                throw Exception(c.getString(R.string.paren_l_over_r))
            } else if (unbalanced) {
                throw Exception(c.getString(R.string.paren_unbalanced))
            }
        }

        // Uses the shunting yard algorithm to generate a stack for the expression that is in postfix notation

        @Throws(Exception::class)
        private fun convertToPostfix(input: String, c: Context): Stack<String> {

            val operationsMap = HashMap<String, Int>()
            operationsMap["-"] = 1
            operationsMap["+"] = 1
            operationsMap["×"] = 2
            operationsMap["÷"] = 2
            operationsMap["^"] = 3

            val stack = Stack<String>()
            val output = Stack<String>()

            // Match all of the possible tokens: either a number, an operator, or ()
            // and put it in an array list
            val allTokens = ArrayList<String>()
            val m = Pattern.compile("[-\\+÷×\\^\\(\\)]|-?\\d+(\\.\\d+)?|-?\\.\\d+|arcsin|arccos|arctan|sin|cos|tan|").matcher(input)
            while (m.find()) {
                allTokens.add(m.group())
            }
            if (allTokens.isEmpty()) {
                return output
            } else if (operationsMap.containsKey(allTokens[0])) {
                throw Exception(c.getString(R.string.err_unexpected) + allTokens[0] + "”")
            }

            var prevToken = ""
            for (token in allTokens) {

                if (token.isEmpty()) {
                    continue
                }

                // if the token is an operator, try to do some stack stuff
                if (operationsMap.containsKey(token)) {

                    while (stack.size > 0) {

                        val prev = stack.peek()

                        // handle an error where two consecutive operators are typed in
                        if (operationsMap.containsKey(prevToken)) {
                            throw Exception(c.getString(R.string.two_consec) + prev + "” and ‟" + token + "”")
                        }

                        // left-associative operations
                        if (token == "-" || token == "+" || token == "÷" || token == "×") {
                            if (prev == "(") {
                                break
                            } else if (operationsMap[token]!! <= operationsMap[prev]!!) {
                                output.push(stack.pop())
                            } else {
                                break
                            }
                        } else if (token == "^") {
                            if (prev == "(") {
                                break
                            } else if (operationsMap[token]!! < operationsMap[prev]!!) {
                                output.push(stack.pop())
                            } else {
                                break
                            }
                        }// right-associative operations
                    }
                    stack.push(token)
                } else if (token.matches("sin|cos|tan|arcsin|arccos|arctan".toRegex())) {
                    stack.push(token)
                } else if (token == ")") {
                    while (stack.peek() != "(") {
                        output.push(stack.pop())
                    }
                    try {
                        stack.pop()
                    } catch (e: EmptyStackException) {
                        throw Exception(c.getString(R.string.paren_generic))
                    }

                } else if (token == "(") {
                    if (isNumeric(prevToken) || prevToken == ")") {
                        stack.push("×")
                    }
                    stack.push(token)
                } else {
                    output.push(token)
                }// if the token is a number, push it to the output
                // if a left ( or a function, push the token directly to the stack

                // store the previous token to check for implied multiply with ()
                prevToken = token
            }

            // clear out the stack
            while (stack.size > 0) {
                output.push(stack.pop())
            }

            return output
        }

        // Evaluates a postfix expression

        @Throws(Exception::class)
        private fun evaluatePostfix(postfixIn: Stack<String>, c: Context): String? {

            if (postfixIn.isEmpty()) {
                return null
            }

            val reversedPostfix = Stack<String>()
            val stack = Stack<BigDecimal>()
            // reverse the stack
            while (postfixIn.size > 0) {
                reversedPostfix.push(postfixIn.pop())
            }

            while (reversedPostfix.size > 0) {
                val token = reversedPostfix.pop()
                if (isNumeric(token)) {
                    val bdToken = BigDecimal(token)
                    stack.push(bdToken)
                } else if (token.matches("[-\\+÷×\\^]".toRegex())) {
                    val operationList = ArrayList<BigDecimal>()

                    when (stack.size) {
                        0 -> throw Exception(c.getString(R.string.nothing_to_op) + token + "” on")
                        1 -> throw Exception(c.getString(R.string.err_unexpected) + token + "”")
                    }
                    for (i in 1..2) {
                        operationList.add(stack.pop())
                    }

                    when (token) {
                        "+" -> stack.push(Operations.addValues(operationList))
                        "-" -> stack.push(Operations.subtractValues(operationList))
                        "×" -> {
                            val multResult = Operations.multiplyValues(operationList)
                            stack.push(multResult)
                        }
                        "÷" -> {
                            val divResult = Operations.divideValues(operationList)
                            stack.push(divResult)
                        }
                        "^" -> stack.push(Operations.exponentiateValues(operationList))
                    }
                } else if (token.matches("sin|cos|tan|arcsin|arccos|arctan".toRegex())) {
                    try {
                        when (token) {
                            "sin" -> stack.push(Operations.calculateSin(stack.pop()))
                            "cos" -> stack.push(Operations.calculateCos(stack.pop()))
                            "tan" -> stack.push(Operations.calculateTan(stack.pop()))
                            "arcsin" -> stack.push(Operations.calculateASin(stack.pop()))
                            "arccos" -> stack.push(Operations.calculateACos(stack.pop()))
                            "arctan" -> stack.push(Operations.calculateATan(stack.pop()))
                        }
                    } catch (e: EmptyStackException) {
                        throw Exception(c.getString(R.string.nothing_to_calc) + token + "\u201d of")
                    }

                } else {
                    throw Exception(c.getString(R.string.unrec_input))
                }
            }

            if (stack.size == 1) {
                val result = stack.pop()
                val roundedResult = result.round(MathContext.DECIMAL32)
                return result.toString()
            } else {
                throw Exception(c.getString(R.string.insufficient_operators))
            }
        }

        private fun isNumeric(str: String): Boolean {
            return str.matches("-?\\d+(\\.\\d+)?|-?\\.\\d+".toRegex()) // match a number, optionally with - and .
        }
    }
}