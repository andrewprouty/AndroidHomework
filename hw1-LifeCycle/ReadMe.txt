Andrew Prouty
	CS 646 Android Mobile Application Development Spring Semester, 2014
	Homework 1, due 2/2/14
	Professor Roger Whitney

Responses to the Issues below
#1
Yes prior to turning in, but not initially.
Initially it was reset with rotation (until #2 below) and even then it did not reflect the onCreate or onStart which are called as a part of the rotation.  My solution to address the 2nd problem was within onRestoreInstanceState to add the restored value to the count; rather than strictly overwriting the counters with the restored value from the saved bundle.  At the time of onRestoreInstanceState the counts were re-initialized to zero so the only activity reflected in the counters are those which otherwise would be missed due to their timing being prior to onRestoreInstanceState being executed.

#2
The values are now maintained across the rotation (destroy/create) thanks to onRestoreInstanceState and onSaveInstanceState.
Regarding display the rotation did not initially reflecting all the values correctly.  Previous to supporting rotation I only setText() when incrementing a counter.  This was not sufficient with rotation. I resolved by resetting all counter fields after the rotation (in onResume).
I used onResume rather than at the end of onRestoreInstanceState, as it seemed like a possible next feature would be to persist the counts across backarrow/re-launch, which does not work at the moment as that flow does not use onRestoreInstanceState.  The future logic for that use case would necessarily be outside of onSaveInstanceState, so having within onResume might be correct for that scenario too.

#3
Even before rotation the label/counter fields as EditText are slightly indented and underlined.  Plus the field values are editable.
On the counter field - can edit but rotation re-asserts the expected counter value due to program logic.
On the label field - when modifying the string value and rotating, the modified value persists across the rotation. As this is the same resourceID this makes sense too.


Issues
1. When you run your app and rotate the device/emulator are the method counts counts consistent with the number of times you see the methods called in the log?

2. Save the values of your field that count the times each method is called using onRestoreInstanceState and onSaveInstanceState. See below for an example of saving and restoring a field. 
How does this affect the displayed values when you rotate the device/emulator?

3. Change one or all of the EditViews to a TextView. After all the user does not need to edit
the values of the count. How does this affect the displayed values when you rotate the
device/emulator?