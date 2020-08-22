package gin.edit.modifynode;

import java.util.Random;

import com.github.javaparser.ast.expr.Expression;

import gin.SourceFile;
import gin.SourceFileTree;
import gin.edit.Edit;

public class ReplaceExpression extends ModifyNodeEdit {

    public String sourceFilename;
    public int sourceExpression;
    public String destinationFilename;
    public int destinationExpression;

    /** 
     * create a random replaceexpression for the given sourcefile, using the provided RNG
     * @param sourceFile to create an edit for
     * @param rng random number generator, used to choose the target expressions
     * */
    public ReplaceExpression(SourceFile sourceFile, Random rng) {
        SourceFileTree sf = (SourceFileTree)sourceFile;
        
        sourceFilename = sourceFile.getFilename();
        destinationFilename = sourceFile.getFilename();
        
        // source can be anywhere in the class
        sourceExpression = sf.getRandomExpressionID(false, rng);
        
        // target is in target method only
        destinationExpression = sf.getRandomExpressionID(true, rng);
    }
    
    public ReplaceExpression(String sourceFilename, int sourceExpression, String destinationFilename, int destinationExpression) {
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
        
        
        // we clone the replacement node, so we don't end up getting confused between the two (that would prevent us replacing expressions within the same parent node)
        try {
            return sf.replaceNode(destinationExpression, source.clone());
        } catch (ClassCastException e) { // JavaParser sometimes throws this if the expressions don't match
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + " " + sourceFilename + ":" + sourceExpression + " -> " + destinationFilename + ":" + destinationExpression;
    }

    public static Edit fromString(String description) {
        String tokens[] = description.split("\\s+");
        String srcTokens[] = tokens[1].split(":");
        String srcFilename = srcTokens[0];
        int source = Integer.parseInt(srcTokens[1]);
        String destTokens[] = tokens[3].split(":");
        String destFilename = destTokens[0];
        int destination = Integer.parseInt(destTokens[1]);
        return new ReplaceExpression(srcFilename, source, destFilename, destination);
    }
    
}
