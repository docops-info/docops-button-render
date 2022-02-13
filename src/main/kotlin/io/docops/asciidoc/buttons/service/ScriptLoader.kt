/*
 * Copyright 2020 The DocOps Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.docops.asciidoc.buttons.service

import java.lang.RuntimeException
import java.lang.StringBuilder
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.StringScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.util.isError
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

class ScriptLoader {
    inline fun <reified T> parseKotlinScript(source: String): T {
        val compilationConfig = createJvmCompilationConfigurationFromTemplate<Any> {
            jvm {
                dependenciesFromCurrentContext(wholeClasspath = true)
            }
        }
        val target = BasicJvmScriptingHost().eval(StringScriptSource(source), compilationConfig, null)
        if(!target.isError()) {
            val obj = (target as ResultWithDiagnostics.Success<EvaluationResult>).value
            val ret = (obj.returnValue as ResultValue.Value).value
            ret?.let {
                return ret as T
            }
            throw RuntimeException("Parsing source failed \n $source")
        } else {
            val sb = StringBuilder()
            target.reports.forEach { scriptDiagnostic -> sb.append(scriptDiagnostic.message) }
            throw RuntimeException(sb.toString())
        }
    }
}