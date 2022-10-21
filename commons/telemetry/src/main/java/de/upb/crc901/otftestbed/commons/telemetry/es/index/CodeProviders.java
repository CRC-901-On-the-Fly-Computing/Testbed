package de.upb.crc901.otftestbed.commons.telemetry.es.index;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import de.upb.crc901.otftestbed.commons.telemetry.es.property.Code;
import de.upb.crc901.otftestbed.commons.telemetry.es.property.CodeProvider;

/**
 * This index stores the code providers and their provided code.
 *
 * @author Roman
 *
 */
@Document(indexName = CodeProviders.INDEX_NAME, type = Index.DEFAULT_TYPE)
public class CodeProviders extends Index<CodeProviders> {

	public static final String INDEX_NAME = Index.DEFAULT_INDEX_PREFIX + "providers_code";


	@Field(type = FieldType.Object)
	private CodeProvider codeProvider;

	@Field(type = FieldType.Object)
	private Code code;


	public CodeProvider getCodeProvider() {
		return codeProvider;
	}

	public void setCodeProvider(CodeProvider codeProvider) {
		this.codeProvider = codeProvider;
	}

	public CodeProviders codeProvider(CodeProvider codeProvider) {
		setCodeProvider(codeProvider);
		return this;
	}


	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public CodeProviders code(Code code) {
		setCode(code);
		return this;
	}
}
