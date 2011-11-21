/*
 * Copyright (c) 2011 Socialize Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialize.Socialize;
import com.socialize.auth.AuthProviderType;
import com.socialize.entity.User;
import com.socialize.ui.SocializeUI;
import com.socialize.ui.button.SocializeButton;
import com.socialize.util.Drawables;
import com.socialize.util.SafeBitmapDrawable;
import com.socialize.util.StringUtils;
import com.socialize.view.BaseView;

/**
 * @author Jason Polites
 *
 */
public class ProfileContentView extends BaseView {

	private ImageView profilePicture;
	private TextView displayName;
	private EditText displayNameEdit;
	private AutoPostFacebookOption autoPostFacebook;
	private SocializeButton facebookSignOutButton;
	private SocializeButton facebookSignInButton;
	
	private SocializeButton editButton;
	private SocializeButton saveButton;
	private SocializeButton cancelButton;
	private ProfileImageContextMenu contextMenu;
	
	private String userDisplayName;
	
	private SafeBitmapDrawable profileDrawable;
	private SafeBitmapDrawable originalProfileDrawable;
	private Drawables drawables;
	
	private User currentUser;
	private boolean isLoggedOnUser = false;
	private Bitmap tmpProfileImage;
	
	private boolean editMode = false;
	
	public ProfileContentView(Context context) {
		super(context);
	}

	public ImageView getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(ImageView profilePicture) {
		this.profilePicture = profilePicture;
	}

	public TextView getDisplayName() {
		return displayName;
	}

	public void setDisplayName(TextView displayName) {
		this.displayName = displayName;
	}
	
	public void setDisplayNameEdit(EditText displayNameEdit) {
		this.displayNameEdit = displayNameEdit;
	}

	public Drawable getProfileDrawable() {
		return profileDrawable;
	}

	public void setProfileDrawable(SafeBitmapDrawable profileDrawable) {
		this.profileDrawable = profileDrawable;
		this.originalProfileDrawable = profileDrawable;
	}

	public SocializeButton getFacebookSignOutButton() {
		return facebookSignOutButton;
	}
	
	public EditText getDisplayNameEdit() {
		return displayNameEdit;
	}

	public void setFacebookSignOutButton(SocializeButton facebookSignOutButton) {
		this.facebookSignOutButton = facebookSignOutButton;
	}
	
	public void setFacebookSignInButton(SocializeButton facebookSignInButton) {
		this.facebookSignInButton = facebookSignInButton;
	}

	public void setUserDisplayName(String name) {
		this.userDisplayName = name;
		revertUserDisplayName();
	}
	
	public SocializeButton getEditButton() {
		return editButton;
	}

	public void setEditButton(SocializeButton editButton) {
		this.editButton = editButton;
	}

	public SocializeButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(SocializeButton saveButton) {
		this.saveButton = saveButton;
	}
	
	public SocializeButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(SocializeButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	public void revertUserDisplayName() {
		if(!StringUtils.isEmpty(userDisplayName)) {
			displayNameEdit.setText(userDisplayName);
		}
		else {
			displayNameEdit.setText("");
		}
	}
	
	public Drawables getDrawables() {
		return drawables;
	}

	public void setDrawables(Drawables drawables) {
		this.drawables = drawables;
	}
	
	public void onFacebookChanged() {
		if(SocializeUI.getInstance().isFacebookSupported() && isLoggedOnUser) {
			if(Socialize.getSocialize().isAuthenticated(AuthProviderType.FACEBOOK)) {
				facebookSignOutButton.setVisibility(View.VISIBLE);
				facebookSignInButton.setVisibility(View.GONE);
				autoPostFacebook.setVisibility(View.VISIBLE);
			}
			else {
				facebookSignOutButton.setVisibility(View.GONE);
				facebookSignInButton.setVisibility(View.VISIBLE);
				autoPostFacebook.setVisibility(View.GONE);
			}
		}
		else {
			facebookSignOutButton.setVisibility(View.GONE);
			facebookSignInButton.setVisibility(View.GONE);
			autoPostFacebook.setVisibility(View.GONE);
		}
	}
	
	public void onProfilePictureChange(Bitmap bitmap) {
		if(bitmap != null) {
			if(tmpProfileImage != null && !tmpProfileImage.isRecycled()) {
				tmpProfileImage.recycle();
			}
			tmpProfileImage = bitmap;
			profileDrawable = new SafeBitmapDrawable(bitmap);
			onEdit();
		}
	}
	
	public boolean getUpdatedAutoPostFBPreference() {
		return autoPostFacebook.isChecked();
	}
	
	public void setAutoPostFacebook(AutoPostFacebookOption autoPostFacebook) {
		this.autoPostFacebook = autoPostFacebook;
	}

	/**
	 * Returns the newly updated user profile name.
	 * @return
	 */
	public String getUpdatedUserDisplayName() {
		return displayNameEdit.getText().toString();
	}
	
	/**
	 * Returns the newly update profile picture.
	 * @return
	 */
	public Bitmap getUpdatedProfileImage() {
		return tmpProfileImage;
	}
	

	public void setContextMenu(ProfileImageContextMenu contextMenu) {
		this.contextMenu = contextMenu;
	}
	
	public AutoPostFacebookOption getAutoPostFacebook() {
		return autoPostFacebook;
	}

	@Override
	protected void onViewLoad() {
		super.onViewLoad();
		onFacebookChanged();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isLoggedOnUser() {
		return isLoggedOnUser;
	}

	public void setLoggedOnUser(boolean isLoggedOnUser) {
		this.isLoggedOnUser = isLoggedOnUser;
	}

	/**
	 * Called when this control is instructed to enter "edit" mode.
	 */
	public void onEdit() {
		if(profileDrawable != null && isLoggedOnUser) {
			displayName.setVisibility(View.GONE);
			editButton.setVisibility(View.GONE);
			facebookSignOutButton.setVisibility(View.GONE);
			facebookSignInButton.setVisibility(View.GONE);
			
			displayNameEdit.setVisibility(View.VISIBLE);
			saveButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);
			
			displayNameEdit.selectAll();
			
			Drawable[] layers = new Drawable[2];
			layers[0] = profileDrawable;
			layers[1] = drawables.getDrawable("camera.png", DisplayMetrics.DENSITY_DEFAULT, true);
			
			layers[0].setAlpha(64);
			
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			profilePicture.setImageDrawable(layerDrawable);
			profilePicture.getBackground().setAlpha(0);
			
			autoPostFacebook.setEnabled(true);
			
			editMode = true;
		}
	}
	
	public void onCancel() {
		displayNameEdit.setVisibility(View.GONE);
		saveButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		displayName.setVisibility(View.VISIBLE);
		
		if(isLoggedOnUser) {
			editButton.setVisibility(View.VISIBLE);
		}
		
		onFacebookChanged();
	
		if(tmpProfileImage != null && !tmpProfileImage.isRecycled()) {
			tmpProfileImage.recycle();
			tmpProfileImage = null;
		}
		
		profileDrawable = originalProfileDrawable;
		profileDrawable.setAlpha(255);
		
		profilePicture.setImageDrawable(profileDrawable);
		profilePicture.getBackground().setAlpha(255);
		
		revertUserDisplayName();
		
		autoPostFacebook.setEnabled(false);
		
		editMode = false;
	}
	
	public void onSave(User user) {
		
		displayName.setText(user.getDisplayName());
		
		displayNameEdit.setVisibility(View.GONE);
		saveButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		displayName.setVisibility(View.VISIBLE);
		
		if(isLoggedOnUser) {
			editButton.setVisibility(View.VISIBLE);
		}		
		
		onFacebookChanged();
		
		profileDrawable.setAlpha(255);
		
		if(originalProfileDrawable != null && originalProfileDrawable != profileDrawable) {
			// Recycle
			originalProfileDrawable.recycle();
		}
		
		// Make sure we don't recycle temp
		tmpProfileImage = null;
		
		originalProfileDrawable = profileDrawable;
		
		profilePicture.setImageDrawable(getProfileDrawable());
		profilePicture.getBackground().setAlpha(255);
		
		autoPostFacebook.setEnabled(false);
		
		editMode = false;
	}
	
	public void onImageEdit() {
		if(editMode) {
			contextMenu.show();
		}
	}
}