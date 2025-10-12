package kz.education.context;

import kz.education.controller.LabelController;
import kz.education.controller.PostController;
import kz.education.controller.WriterController;
import kz.education.repository.LabelRepository;
import kz.education.repository.PostRepository;
import kz.education.repository.WriterRepository;
import kz.education.repository.impl.GsonLabelRepositoryImpl;
import kz.education.repository.impl.GsonPostRepositoryImpl;
import kz.education.repository.impl.GsonWriterRepositoryImpl;
import kz.education.view.LabelView;
import kz.education.view.PostView;
import kz.education.view.WriterView;

import java.util.Objects;

public class ApplicationContext {
    private static ApplicationContext instance;

    private final WriterView writerView;
    private final LabelView labelView;
    private final PostView postView;

    private ApplicationContext() {
        LabelRepository labelRepository = new GsonLabelRepositoryImpl();
        LabelController labelController = new LabelController(labelRepository);
        this.labelView = new LabelView(labelController);

        PostRepository postRepository = new GsonPostRepositoryImpl();
        PostController postController = new PostController(postRepository);
        this.postView = new PostView(postController, labelController);

        WriterRepository writerRepository = new GsonWriterRepositoryImpl();
        WriterController writerController = new WriterController(writerRepository);
        this.writerView = new WriterView(writerController, postController);
    }

    public static ApplicationContext getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public WriterView getWriterView() {
        return writerView;
    }

    public LabelView getLabelView() {
        return labelView;
    }

    public PostView getPostView() {
        return postView;
    }
}
