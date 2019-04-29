package ufc.br.mutant_project.runners;

import org.apache.commons.io.FileUtils;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;
import ufc.br.mutant_project.constants.PathProject;
import ufc.br.mutant_project.exceptions.PomException;
import ufc.br.mutant_project.models.ParameterProcessorSubProcess;
import ufc.br.mutant_project.processors.AbstractorProcessor;
import ufc.br.mutant_project.processors.AbstractorProcessorSubProcess;
import ufc.br.mutant_project.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RunnerSubProcessThrows extends AbstractRunner {

	public RunnerSubProcessThrows(String uriName, String subModule, boolean isMavenProject) {
		super(uriName, subModule, isMavenProject);
	}
	
	@Override
	public void processor(AbstractorProcessor<?> cp1) throws PomException {
		AbstractorProcessorSubProcess<?> cp = (AbstractorProcessorSubProcess<?>) cp1;
		
		File source = new File(PathProject.makePathToJavaCode(uriName, subModule));
		System.out.println("Gerando mutantes nas classes...");
		String[] tiposDeArquivo = new String[] { "java" };

		for (File f : FileUtils.listFiles(source, tiposDeArquivo, true)) {

			SpoonAPI spoon = new Launcher();
			spoon.getEnvironment().setNoClasspath(true);
			spoon.getEnvironment().setCommentEnabled(true);
			spoon.addInputResource(f.getAbsolutePath());

			System.out.println("Arquivo: " + f.getAbsolutePath());

			CtModel mo = spoon.buildModel();

			Map<Integer, Integer> map = new HashMap();

			int qtd = getQtdAndOccurrencesMethodsThrows(mo, map);

			System.out.println("Número de ocorrencias CtMethod: " + qtd);

			for (int i = 1; i < qtd; i++) {
				if(map.containsKey(i)) {
					int qtdItems = map.get(i);
					for(int j=1; j <= qtdItems; j++) {
						
						Util.clearOutputSpoonToProject();
		
						spoon = new Launcher();
						spoon.getEnvironment().setNoClasspath(true);
						spoon.getEnvironment().setCommentEnabled(true);
						spoon.getEnvironment().setPreserveLineNumbers(true);
						spoon.getEnvironment().setAutoImports(false);
						spoon.addInputResource(f.getAbsolutePath());
						spoon.setSourceOutputDirectory(PathProject.getPathTemp());
		
						ParameterProcessorSubProcess pv = new ParameterProcessorSubProcess();
						pv.setPosition(i);
						pv.setPositionProcess(j);
						cp.resetPosition();
						cp.setUriName(uriName);
						cp.resetPositionProcess();
						cp.setParameterVisitor(pv);
						cp.setSubModule(subModule);
						cp.setMavenProject(isMavenProject);

						spoon.addProcessor(cp);
						spoon.run();
						
						if (cp.getParameterVisitor().isNeedModification()) {
							System.out.println("Posicao for " + i + " Número do mutante: " + numberMutant
							+ " precisa de modificação: " + cp.getParameterVisitor().isNeedModification());

							System.out.println("Saída do arquivo modificado: \n" + cp.getParameterVisitor());
							saveMutant(f, cp);
						}
					}
				}
			}
		}
		
		if(numberMutant > 0)
			finalResult(cp);
		else
			System.out.println("Não foi necessário criar nenhum mutante do tipo " +cp.pathIdentification() + " para o projeto: "+uriName);
		
		resetResults();
	}

	private int getQtdAndOccurrencesMethodsThrows(CtModel model, Map map){
		final int[] count = {1};
		model.getElements((Filter<CtMethod>) e ->{
			if(e.getThrownTypes()!=null && e.getThrownTypes().size()>0){
				map.put(count[0], e.getThrownTypes().size());
				count[0]++;
			}
			return true;
		});

		return count[0];
	}
}