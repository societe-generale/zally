package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import de.zalando.zally.utils.PatternUtil.isVersion
import io.swagger.models.Swagger
import org.springframework.stereotype.Component

@Component
class VersionInInfoSectionRule : AbstractRule() {
    override val title = "Provide version information"
    override val url = "http://zalando.github.io/restful-api-guidelines/compatibility/Compatibility.html" +
            "#should-provide-version-information-in-openapi-documentation"
    override val violationType = ViolationType.SHOULD
    private val DESCRIPTION = "Only the documentation, not the API itself, needs version information. It should be in the " +
            "format MAJOR.MINOR.DRAFT."

    override fun validate(swagger: Swagger): Violation? {
        val version = swagger.info?.version
        val desc = when {
            version == null -> "Version is missing"
            !isVersion(version) -> "Specified version has incorrect format: $version"
            else -> null
        }
        return desc?.let { Violation(this, title, "$DESCRIPTION $it", violationType, url, emptyList()) }
    }
}