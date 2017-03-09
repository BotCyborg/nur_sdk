/* 
  Copyright 2016- Nordic ID
  NORDIC ID SOFTWARE DISCLAIMER

  You are about to use Nordic ID Demo Software ("Software"). 
  It is explicitly stated that Nordic ID does not give any kind of warranties, 
  expressed or implied, for this Software. Software is provided "as is" and with 
  all faults. Under no circumstances is Nordic ID liable for any direct, special, 
  incidental or indirect damages or for any economic consequential damages to you 
  or to any third party.

  The use of this software indicates your complete and unconditional understanding 
  of the terms of this disclaimer. 
  
  IF YOU DO NOT AGREE OF THE TERMS OF THIS DISCLAIMER, DO NOT USE THE SOFTWARE.  
*/
package com.nordicid.nurapi;

public interface NurApiAutoConnectTransport {
	
	public String getType();
	
	public String getDetails();
	
	public void setAddress(String addr);
	
	public String getAddress();

	public void onPause();
	
	public void onResume();
	
	public void onStop();
	
	public void onDestroy();
	
	public void dispose();
}
