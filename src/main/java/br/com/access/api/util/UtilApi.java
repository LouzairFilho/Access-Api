package br.com.access.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
public class UtilApi {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ServletContext servletContent;
	
	private final String EXTENSAO_LOG = "log";
	
	public String getMessageKey(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}
	
	/**
	 * Valida se objeto esta nulo ou vazio
	 */
	public Boolean isPreenchimentoNuloOuVazio(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof CharSequence) {
			return (((CharSequence) obj).length() == 0);
		} else if (obj instanceof Number) {
			return (((Number) obj).longValue() == 0);
		} else if (obj instanceof Collection<?>) {
			return (((Collection<?>) obj).isEmpty());
		} else if (obj instanceof Map) {
			return (((Map<?, ?>) obj).isEmpty());
		} else if (obj instanceof Object[]) {
			return (((Object[]) obj).length == 0);
		}
		return false;
	}

	
	
	public void fazerUpload(byte[] bytes, String path, String nomeArquivoRenomeado) {
		try {
			String caminhoAbsoluto = "";
			caminhoAbsoluto = path + nomeArquivoRenomeado;
			writeFile(bytes, path, caminhoAbsoluto, nomeArquivoRenomeado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeFile(byte[] content, String path, String filename, String nomeArquivoRenomeado)
			throws IOException {
		File file = new File(filename);
		File file2 = null;
		if (nomeArquivoRenomeado != null && !nomeArquivoRenomeado.isEmpty()) {
			file2 = new File(path.concat(nomeArquivoRenomeado));
			file.renameTo(file2);

			if (!file2.exists()) {
				System.out.println("not exist> " + file2.getAbsolutePath() + " new File");
				file2.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file2);
			fop.write(content);
			fop.flush();
			fop.close();
		} else {
			if (!file.exists()) {
				System.out.println("not exist> " + file.getAbsolutePath() + " new File");
				file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file);
			fop.write(content);
			fop.flush();
			fop.close();
		}
	}

	public String getExtensao(String nomeArquivo) {
        String format = "";
        int end = nomeArquivo.lastIndexOf(".");
        if (end != -1) format = nomeArquivo.substring(end + 1, nomeArquivo.length());
		return format;
	}
	
	public Boolean temExtensaoPerfil(String extensao){
		List<String> extensaoPerfil = Arrays.asList(EXTENSAO_LOG);
		return extensaoPerfil.contains(extensao);
	}
	
	public String getPathUpload() {
		String caminho = new File("").getAbsolutePath().concat("/src/uploads/");;
		return caminho;
	}
	
	public String gerarIdentificadorAleatorio(String split) {
		UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		return Long.toString(l, Character.MAX_RADIX).toString().concat(split);
	}
}
