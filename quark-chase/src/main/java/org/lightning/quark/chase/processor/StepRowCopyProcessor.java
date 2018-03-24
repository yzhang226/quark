package org.lightning.quark.chase.processor;

import com.google.common.collect.Lists;
import org.lightning.quark.chase.copy.DataRowCopier;
import org.lightning.quark.core.feature.RowFeature;
import org.lightning.quark.core.message.BaseActionProcessor;
import org.lightning.quark.core.message.ProcessResult;
import org.lightning.quark.core.model.message.MessageFeature;
import org.lightning.quark.core.row.StepRowCopyRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by cook on 2018/3/21
 */
public class StepRowCopyProcessor extends BaseActionProcessor<StepRowCopyRequest> {

    private static final Logger logger = LoggerFactory.getLogger(StepRowCopyProcessor.class);

    private static final List<MessageFeature> features = Lists.newArrayList(RowFeature.V1.STEP_ROW_COPY);

    private DataRowCopier dataRowCopier;

    public StepRowCopyProcessor(DataRowCopier dataRowCopier) {
        this.dataRowCopier = dataRowCopier;
    }

    @Override
    protected ProcessResult processResource(StepRowCopyRequest resource) {
        dataRowCopier.copyByStep(resource.getStartPk(), resource.getPageSize());
        logger.debug("copy step data done, pk#{}, pageSize#{}", resource.getStartPk(), resource.getPageSize());
        return ProcessResult.ok();
    }

    @Override
    public List<MessageFeature> getMessageFeatures() {
        return features;
    }
}
