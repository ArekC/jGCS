package fitness;

import cyk.CykResult;
import dataset.Sequence;
import executionTime.ExecutionTimeService;
import grammar.Grammar;
import rulesTable.RulesTable;

public class FitnessService {

    private ExecutionTimeService executionTimeService = ExecutionTimeService.getInstance();

    private static FitnessService instance;

    private FitnessService() {
    }

    public static FitnessService getInstance() {
        if (instance == null)
            instance = new FitnessService();
        return instance;
    }

    public void countRulesFitness(Grammar grammar) {
        grammar.getNonTerminalRules().forEach(rule -> {
            double fitness = 0.0;
            if (((double) rule.getCountUsageInValidSentencesParsing() +  (double) rule.getCountUsageInNotValidSentencesParsing()) > 0) {
                fitness = rule.getCountUsageInValidSentencesParsing() / ((double) rule.getCountUsageInValidSentencesParsing() + (double) rule.getCountUsageInNotValidSentencesParsing());
            }
            rule.setFitness(fitness);
        });

    }

    public void countRulesUsage(Sequence sequence, CykResult cykResult, Grammar grammar) {
        RulesTable rulesTable = cykResult.getRulesTable();

        grammar.getNonTerminalRules().forEach(rule -> {
            for (int i = 1; i < rulesTable.getLength() - 1; i++) {
                for (int j = 0; j < rulesTable.getLength() - i; j++) {
                    if (rulesTable.getCellRule(i, j, rule) != null) {
                        if (sequence.isPositive()) {
                            rule.incrementUsageInValidSentencesParsing();
                        }
                        if (!sequence.isPositive()) {
                            rule.incrementUsageInNotValidSentencesParsing();
                        }
                    }
                }
            }
        });
    }
}
