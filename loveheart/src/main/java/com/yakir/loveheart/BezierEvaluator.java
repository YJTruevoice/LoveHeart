package com.yakir.loveheart;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 自定义估值器
 * Created by on 2016年03月28日.
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF p1;
    private PointF p2;

    public BezierEvaluator(PointF p1, PointF p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public PointF evaluate(float t, PointF p0, PointF p3) {

        PointF pointF = new PointF();

        // 实现贝塞尔公式
        pointF.x = p0.x * (1 - t) * (1 - t) * (1 - t) + 3 * p1.x * t * (1 - t) * (1 - t) + 3 * p2.x * t * t * (1 - t) + p3.x * t * t * t;
        pointF.y = p0.y * (1 - t) * (1 - t) * (1 - t) + 3 * p1.y * t * (1 - t) * (1 - t) + 3 * p2.y * t * t * (1 - t) + p3.y * t * t * t;

        return pointF;
    }
}
