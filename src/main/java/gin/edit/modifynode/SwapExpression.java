package gin.edit.modifynode;

import java.util.Random;

import com.github.javaparser.ast.expr.Expression;

import gin.SourceFile;
import gin.SourceFileTree;
import gin.edit.Edit;

public class SwapExpression extends ModifyNodeEdit {

    public String sourceFilename;
    public int sourceExpression;
    public String destinationFilename;
    public int destinationExpression;

    /** 
     * create a random swapexpression for the given sourcefile, using the provided RNG
     * @param sourceFile to create an edit for
     * @param rng random number generator, used to choose the target expressions
     * */
    public SwapExpression(SourceFile sourceFile, Random rng) {
        
        SourceFileTree sf = (SourceFileTree)sourceFile;
        
        sourceFilename = sf.getFilename();
        destinationFilename = sf.getFilename();
        
        // source and target in target method only
        sourceExpression = sf.getRandomExpressionID(true, rng);
        destinationExpression = sf.getRandomExpressionID(true, rng);

    }
    
    public SwapExpression(String sourceFilename, int sourceExpression, String destinationFilename, int destinationExpression) {
        this.sourceFilename = sourceFilename;
        this.sourceExpression = sourceExpression;
        this.destinationFilename = destinationFilename;
        this.destinationExpression = destinationExpression;
    }
    
    @Override
    public SourceFile apply(SourceFile sourceFile) {

        if (sourceExpression == destinationExpression) {
            return sourceFile; // no-op
        }
        
        SourceFileTree sf = (SourceFileTree)sourceFile;
        
        Expression source = sf.getExpression(sourceExpression);
        Expression destination = sf.getExpression(destinationExpression);
            
        if ((source == null) || (destination == null)) {
            return sf; // targeting a deleted location just does nothing.
        }
        
        try {
            // we clone the replacement nodes, so we don't end up getting confused between the two (that would prevent us swapping expressions within the same parent node)
            sf = sf.replaceNode(sourceExpression, destination.clone());
            sf = sf.replaceNode(destinationExpression, source.clone());
            return sf;
        } catch (ClassCastException e) { // JavaParser sometimes throws this if the expressions don't match
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + " " + sourceFilename + ":" + sourceExpression + " <-> " + destinationFilename + ":" + destinationExpression;
    }

    public static Edit fromString(String description) {
        String tokens[] = description.split("\\s+");
        String srcTokens[] = tokens[1].split(":");
        String srcFilename = srcTokens[0];
        int source = Integer.parseInt(srcTokens[1]);
        String destTokens[] = tokens[3].split(":");
        String destFilename = destTokens[0];
        int destination = Integer.parseInt(destTokens[1]);
        return new SwapExpression(srcFilename, source, destFilename, destination);
    }
    
}
