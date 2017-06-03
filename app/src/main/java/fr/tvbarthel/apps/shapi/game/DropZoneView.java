package fr.tvbarthel.apps.shapi.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import fr.tvbarthel.apps.shapi.R;
import fr.tvbarthel.apps.shapi.core.ShapiApplication;
import fr.tvbarthel.apps.shapi.shape.Circle;
import fr.tvbarthel.apps.shapi.shape.Diamond;
import fr.tvbarthel.apps.shapi.shape.Rectangle;
import fr.tvbarthel.apps.shapi.shape.Shape;
import fr.tvbarthel.apps.shapi.shape.ShapeView;
import fr.tvbarthel.apps.shapi.shape.Triangle;
import fr.tvbarthel.apps.shapi.ui.drag.DragHelper;
import fr.tvbarthel.apps.shapi.ui.drag.DragListener;

/**
 * A {@link android.view.View} representing a {@link DropZone}.
 */
public class DropZoneView extends FrameLayout {

    /**
     * The duration of the scale animation (in milliseconds).
     */
    private static final long SCALE_ANIMATION_DURATION = 300;

    /**
     * Drop zone presenter.
     */
    @Inject
    protected DropZoneContract.Presenter presenter;

    /**
     * An internal {@link ShapeView}.
     */
    private ShapeView shapeView;
    private DropZoneContract.View internalView;
    private Listener listener;
    private DropZone dropZone;
    private Interpolator scaleInterpolator;
    private DragHelper dragHelper;
    private DragListener dragListener;

    /**
     * Create a {@link DropZoneView}
     *
     * @param context a {@link Context}
     */
    public DropZoneView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Create a {@link DropZoneView}
     *
     * @param context a {@link Context}
     * @param attrs   an {@link AttributeSet}
     */
    public DropZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Create a {@link DropZoneView}.
     *
     * @param context      a {@link Context}
     * @param attrs        an {@link AttributeSet}
     * @param defStyleAttr the def style attribute
     */
    public DropZoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachView(internalView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView(internalView);
    }

    /**
     * Set the {@link DropZone} that should be represented
     *
     * @param dropZone a {@link DropZone}
     */
    public void setDropZone(DropZone dropZone) {
        this.dropZone = dropZone;
        final Shape shapeForDropZone = getShapeForDropZone(dropZone);
        shapeView.setShape(shapeForDropZone);
    }

    /**
     * Listener used to catch view events.
     *
     * @param listener listener used to catch view events.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Shape getShapeForDropZone(DropZone dropZone) {
        final Class<? extends Shape> dropZoneShape = dropZone.getShape();
        if (dropZoneShape == Rectangle.class) {
            return new Rectangle();
        } else if (dropZoneShape == Triangle.class) {
            return new Triangle();
        } else if (dropZoneShape == Circle.class) {
            return new Circle();
        } else if (dropZoneShape == Diamond.class) {
            return new Diamond();
        } else {
            throw new IllegalArgumentException("Unsupported drop zone. Found: " + dropZoneShape);
        }
    }

    private void onDragExited() {
        animateScale(1.0f);
    }

    private void onDragEntered() {
        animateScale(1.2f);
    }

    private void onShapeDropped(Shape data) {
        animateScale(1.0f);
        if (listener != null) {
            listener.onShapeDropped(dropZone, data);
        }
    }

    private void animateScale(float scaleFactor) {
        DropZoneView.this.animate()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .setDuration(SCALE_ANIMATION_DURATION)
                .setInterpolator(scaleInterpolator)
                .start();
    }

    /**
     * Initialize internal component.
     *
     * @param context holding context.
     */

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_drop_zone_box, this);
        ShapiApplication.component().inject(this);

        shapeView = ((ShapeView) findViewById(R.id.view_drop_zone_box_internal_shape));

        scaleInterpolator = new OvershootInterpolator();
        dragHelper = DragHelper.getInstance();

        initializeInternalListeners();
    }

    private void initializeInternalListeners() {
        dragListener = new DragListener() {
            @Override
            protected void onDragDropped(View source, Object data) {
                super.onDragDropped(source, data);
                DropZoneView.this.onShapeDropped((Shape) data);
            }

            @Override
            protected void onDragEntered(View source, Object data) {
                super.onDragEntered(source, data);
                DropZoneView.this.onDragEntered();
            }

            @Override
            protected void onDragExited(View source, Object data) {
                super.onDragExited(source, data);
                DropZoneView.this.onDragExited();
            }
        };

        internalView = new DropZoneContract.View() {
            @Override
            public void registerDragListener(ArrayList<Class<?>> availableShapes) {
                dragHelper.register(DropZoneView.this, dragListener, availableShapes);
            }

            @Override
            public void unRegisterDragListener() {
                DropZoneView.this.setOnDragListener(null);
            }
        };
    }

    /**
     * Listener used to catch view events.
     */
    public interface Listener {

        /**
         * Called when the user dropped a shape in the given drop zone.
         *
         * @param dropZone zone in which the user dropped the shape.
         * @param shape    shape dropped by the user.
         */
        void onShapeDropped(@NonNull DropZone dropZone, @Nullable Shape shape);
    }

}