/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * Copyright 2013 Naver Business Platform Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.IIndicatorLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

@SuppressLint("ViewConstructor")
public class DefaultIndicatorLayout extends IndicatorLayout implements IIndicatorLayout, AnimationListener {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private Animation mInAnim, mOutAnim;
	private ImageView mArrowImageView;

	private final Animation mRotateAnimation, mResetRotateAnimation;

	public DefaultIndicatorLayout(Context context, PullToRefreshBase.Mode mode) {
		super(context);
		mArrowImageView = new ImageView(context);

		Drawable arrowD = getIconDrawable(context, mode);
		mArrowImageView.setImageDrawable(arrowD);
		
		final int padding = getResources().getDimensionPixelSize(R.dimen.indicator_internal_padding);
		mArrowImageView.setPadding(padding, padding, padding, padding);
		addView(mArrowImageView);

		int inAnimResId, outAnimResId;
		switch (mode) {
			case PULL_FROM_END:
				inAnimResId = R.anim.slide_in_from_bottom;
				outAnimResId = R.anim.slide_out_to_bottom;
				setBackgroundResource(R.drawable.indicator_bg_bottom);

				// Rotate Arrow so it's pointing the correct way
				mArrowImageView.setScaleType(ScaleType.MATRIX);
				Matrix matrix = new Matrix();
				matrix.setRotate(180f, arrowD.getIntrinsicWidth() / 2f, arrowD.getIntrinsicHeight() / 2f);
				mArrowImageView.setImageMatrix(matrix);
				break;
			default:
			case PULL_FROM_START:
				inAnimResId = R.anim.slide_in_from_top;
				outAnimResId = R.anim.slide_out_to_top;
				setBackgroundResource(R.drawable.indicator_bg_top);
				break;
		}

		mInAnim = AnimationUtils.loadAnimation(context, inAnimResId);
		mInAnim.setAnimationListener(this);

		mOutAnim = AnimationUtils.loadAnimation(context, outAnimResId);
		mOutAnim.setAnimationListener(this);

		final Interpolator interpolator = new LinearInterpolator();
		mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(interpolator);
		mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mResetRotateAnimation.setInterpolator(interpolator);
		mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

	}
	/**
	 * Create an icon that default indicator layout will use 
	 * @param context
	 * @param mode
	 * @return Indicator icon
	 */
	protected Drawable getIconDrawable(Context context, PullToRefreshBase.Mode mode) {
		return getResources().getDrawable(R.drawable.indicator_arrow);
	}
	/**
	 * {@inheritDoc}
	 */
	public final boolean isVisible() {
		Animation currentAnim = getAnimation();
		if (null != currentAnim) {
			return mInAnim == currentAnim;
		}

		return getVisibility() == VISIBLE;
	}
	/**
	 * {@inheritDoc}
	 */
	public void hide() {
		startAnimation(mOutAnim);
	}
	/**
	 * {@inheritDoc}
	 */
	public void show() {
		mArrowImageView.clearAnimation();
		startAnimation(mInAnim);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == mOutAnim) {
			mArrowImageView.clearAnimation();
			setVisibility(GONE);
		} else if (animation == mInAnim) {
			setVisibility(VISIBLE);
		}

		clearAnimation();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// NO-OP
	}

	@Override
	public void onAnimationStart(Animation animation) {
		setVisibility(VISIBLE);
	}
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void releaseToRefresh() {
		mArrowImageView.startAnimation(mRotateAnimation);
	}
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void pullToRefresh() {
		mArrowImageView.startAnimation(mResetRotateAnimation);
	}

	/**
	 * {@inheritDoc}
	 */
	public LayoutParams createApplicableHeaderLayoutParams() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.indicator_right_padding);
		params.gravity = Gravity.TOP | Gravity.RIGHT;
		return params;
	}
	/**
	 * {@inheritDoc}
	 */
	public LayoutParams createApplicableFooterLayoutParams() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.rightMargin = getResources().getDimensionPixelSize(R.dimen.indicator_right_padding);
		params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		return params;
	}	
}