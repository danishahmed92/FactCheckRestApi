package wrapper;

import api.ApplicationStartup;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.aksw.defacto.Constants;
import org.aksw.defacto.Defacto;
import org.aksw.defacto.DefactoDemo;
import org.aksw.defacto.config.DefactoConfig;
import org.aksw.defacto.evidence.Evidence;
import org.aksw.defacto.model.DefactoModel;
import org.dice.factcheck.nlp.stanford.*;
import org.dice.factcheck.nlp.stanford.impl.CoreNLPLocalClient;
import org.dice.factcheck.nlp.stanford.impl.CoreNLPServerClient;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.aksw.defacto.DefactoDemo.corenlpClient;
import static org.aksw.defacto.DefactoDemo.getEinsteinModel;

public class FactCheckBytes {

    /*
     *This method is used for fetching results from defacto
     *
     * Input file data is read and converted into model
     * Previously loaded CoreNLP is utilised
     */
    public static Map<DefactoModel, Evidence> FactCheckFromBytes(String taskId, byte[] data) throws IOException {
        String lang = "en";

        List<DefactoModel> models = new ArrayList<>();
        //models.add(bytesToModel(data, taskId, lang));   // taskId = modelName

        DefactoModel model = bytesToModel(data, taskId, lang);
        ApplicationStartup start = new ApplicationStartup();

        model.corenlpClient = start.corenlpClient;

        models.add(model);


        return Defacto.checkFacts(models, Defacto.TIME_DISTRIBUTION_ONLY.NO);
    }

    private static DefactoModel bytesToModel(byte[] data, String modelName, String lang) {
        final Model model = ModelFactory.createDefaultModel();
        model.read(new ByteArrayInputStream(data), null, "TURTLE");

        return new DefactoModel(model, modelName, true, Arrays.asList(lang));
    }
}
