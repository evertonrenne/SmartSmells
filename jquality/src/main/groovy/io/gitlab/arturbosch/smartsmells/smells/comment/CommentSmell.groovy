package io.gitlab.arturbosch.smartsmells.smells.comment

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import groovy.transform.Immutable
import groovy.transform.ToString
import io.gitlab.arturbosch.jpal.ast.ClassHelper
import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.smartsmells.smells.ClassSpecific
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific

/**
 * Represents a comment smell. There are two types of comment smell.
 * One is a comment above private and package private methods.
 * The other is a orphan comment inside a method. Both indicate excuses for a poor code style.
 *
 * @author artur
 */
@Immutable
@ToString(includePackage = false)
class CommentSmell implements DetectionResult, ClassSpecific, MethodSpecific {

	enum Type {
		ORPHAN(CommentConstants.ORPHAN_MESSAGE),
		PRIVATE(CommentConstants.PRIVATE_JAVADOC_MESSAGE),
		MISSING_PARAMETER(CommentConstants.MISSING_PARAM_TAG),
		MISSING_THROWS(CommentConstants.MISSING_PARAM_TAG),
		MISSING_RETURN(CommentConstants.MISSING_RETURN_TAG),
		MISSING_JAVADOC(CommentConstants.MISSING_PUBLIC_JAVADOC)

		Type(String message) {
			this.message = message
		}
		String message
	}

	Type type
	String signature // orphan comments have no signature as they must not be located on a node

	boolean hasTODO
	boolean hasFIXME

	@Delegate
	SourcePath sourcePath
	@Delegate
	SourceRange sourceRange

	ElementTarget elementTarget = ElementTarget.ANY

	@Override
	ElementTarget elementTarget() {
		return elementTarget
	}

	@Override
	String asCompactString() {
		"CommentSmell - $type\n\n$type.message"
	}

	@Override
	String asComparableString() {
		return "${javaClassName()}$type$signature"
	}

	@Override
	String name() {
		return signature
	}

	@Override
	String signature() {
		return signature
	}

	@Override
	MethodSpecific copy(MethodDeclaration method) {
		return new CommentSmell(type, method.declarationAsString, hasTODO, hasFIXME,
				sourcePath, SourceRange.fromNode(method), elementTarget)
	}

	@Override
	ClassSpecific copy(ClassOrInterfaceDeclaration clazz) {
		return new CommentSmell(type, ClassHelper.createFullSignature(clazz), hasTODO, hasFIXME,
				sourcePath, SourceRange.fromNode(clazz), elementTarget)
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (getClass() != o.class) return false

		CommentSmell that = (CommentSmell) o

		if (hasFIXME != that.hasFIXME) return false
		if (hasTODO != that.hasTODO) return false
		if (elementTarget != that.elementTarget) return false
		if (signature != that.signature) return false
		if (sourcePath != that.sourcePath) return false
		if (sourceRange != that.sourceRange) return false
		if (type != that.type) return false

		return true
	}

	int hashCode() {
		int result
		result = (type != null ? type.hashCode() : 0)
		result = 31 * result + (signature != null ? signature.hashCode() : 0)
		result = 31 * result + (hasTODO ? 1 : 0)
		result = 31 * result + (hasFIXME ? 1 : 0)
		result = 31 * result + (sourcePath != null ? sourcePath.hashCode() : 0)
		result = 31 * result + (sourceRange != null ? sourceRange.hashCode() : 0)
		result = 31 * result + (elementTarget != null ? elementTarget.hashCode() : 0)
		return result
	}
}
