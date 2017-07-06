# grails-fb-messenger
A grails plugin for facebook messenger

To use this plugin, simply declare the following in `build.gradle`

```
compile 'org.grails.plugins:fb-messenger:0.1'
```

Create a controller in your grails project, e.g. ``FbController`` and implements ``MessengerWebhook`` like the following

```
class FbController implements MessengerWebhook{
```

You will just need to implement one method called ``receiveMessage`` to further process the event received from facebook.

Please leave a msg here if you want a working sample. I will be glad to provide it on github.
