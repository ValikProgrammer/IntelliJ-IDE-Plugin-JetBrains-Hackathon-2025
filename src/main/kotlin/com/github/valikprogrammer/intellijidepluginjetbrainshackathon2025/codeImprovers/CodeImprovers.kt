package com.github.valikprogrammer.intellijidepluginjetbrainshackathon2025.codeImprovers

interface CodeImprover {
	fun buildPrompt(code: String): String
}

class ReadabilityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You are an expert engineer focused on improving code readability without changing behavior.
Focus on:
- Descriptive names, remove abbreviations
- Consistent formatting and style
- Smaller, focused functions
- Remove dead code and noise
- Minimal comments only where needed to clarify intent

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class EffectivenessImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You improve developer effectiveness while preserving behavior.
Focus on:
- Simplify control flow and APIs
- Reduce boilerplate and repetitive patterns
- Clear error-handling ergonomics
- Favor composition and clarity

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class MaintainabilityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You refactor for long-term maintainability without changing behavior.
Focus on:
- Single Responsibility, extract helpers
- Reduce coupling, increase cohesion
- Clear structure and consistent naming
- Self-documenting code with minimal comments

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class PerformanceImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You optimize performance while preserving behavior.
Focus on:
- Efficient data structures and algorithms
- Reduce allocations and copies
- Avoid unnecessary work and blocking calls
- Cache/results reuse when safe

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class ComplexityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You reduce cognitive and cyclomatic complexity without changing behavior.
Focus on:
- Split long functions
- Flatten nested conditionals and loops
- Early returns/guards
- Remove duplication, linear control flow

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class SecurityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You harden security without changing intended behavior.
Focus on:
- Input validation and sanitization
- Safe defaults, least privilege
- No sensitive info in errors/logs
- Avoid insecure APIs, thread safety where relevant

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class ConsistencyImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You enforce stylistic and architectural consistency.
Focus on:
- Consistent naming, error handling, nullability
- Uniform immutability decisions
- Consistent module boundaries and layout

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class ReusabilityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You improve reusability without over-abstracting.
Focus on:
- Extract reusable helpers/components
- Parameterize magic constants
- Reduce hidden side effects
- Clear, small interfaces

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class TestabilityImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You improve testability while preserving behavior.
Focus on:
- Dependency inversion/injection
- Pure functions and small units
- Deterministic behavior
- Minimize global/static state

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

class DuplicationImprover : CodeImprover {
	override fun buildPrompt(code: String): String = """
System:
You reduce duplication safely without over-engineering.
Focus on:
- Extract shared logic and constants
- Consolidate repeated code
- Simple, readable abstractions only

User:
Return only the improved code (no explanations, no markdown).
Code:
$code
""".trimIndent()
}

object CodeImprovers {
	fun buildPrompt(type: String, code: String): String {
		val improver: CodeImprover = when (type.lowercase()) {
			"readability"     -> ReadabilityImprover()
			"effectiveness"   -> EffectivenessImprover()
			"maintainability" -> MaintainabilityImprover()
			"performance"     -> PerformanceImprover()
			"complexity"      -> ComplexityImprover()
			"security"        -> SecurityImprover()
			"consistency"     -> ConsistencyImprover()
			"reusability"     -> ReusabilityImprover()
			"testability"     -> TestabilityImprover()
			"duplication"     -> DuplicationImprover()
			else              -> ReadabilityImprover()
		}
		return improver.buildPrompt(code)
	}
}
