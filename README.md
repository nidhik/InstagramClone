# InstagramClone
An Android clone of the Instagram app.
# Codepath Task List App

Time spent: about 15-20 hours spent in total 

Completed user stories:

 * [x] Required: User can scroll through current popular photos from Instagram.
 * [x] Required: For each photo displayed, user can see the following details: Graphic, Caption, Username 
 * [x] Optional: Relative timestamp, like count, user profile image.
 * [x] Optional: Add pull-to-refresh for popular stream with SwipeRefreshLayout
 * [x] Optional: Display each user profile image using a RoundedImageView 
 * [x] Optional: Improve the user interface through styling and coloring
 * [x] Anything else that you can get done to improve the app functionality or user experience! - can play/pause the video in the feed itself. Also added the username next to the profile piture as in the Instgram app.


Challenges:

I created a different layouts for list items that displayed a photo vs a video, but I wasn't happy with the way this turned out.

The ViewHolder pattern also becomes slightly less effective with this approach because you may have attached a view of the incorrect type to the recycled list item. For example, you may have to convert a recycled list item that was displaying a photo to display a video.

<b>What is correct approach when you want to display types of views in a list? </b>

It also took me some time to figure out how to remove the pause/play controls of the MediaController from the video view. 
<b>Is there a better way to do this that to just set its visibility to View.GONE?</b>

I ended up overlaying a play button on top of the video and requireing the user to tap it to play the video.
<b>How would you approach playing the video only when it is scrolled onto the screen and stopping it when it is scrolled off?</b>

Notes:

Code improvements I'm aware of that I didn't have time to include:
* Creating a seperate model for InstagramVideo 
* Implement View Holder pattern in adapter
* move networking/deserialization code out of the activity, it doesn't belong there
* Handle exceptions thrown when deserializing the result from the photos endpoint.

Walkthrough of all user stories:

![Video Walkthrough](https://github.com/nidhik/InstagramClone/blob/master/codepath-assignment-week1-android.gif)
GIF created with [LiceCap](http://www.cockos.com/licecap/).
