//
//  lalaPlugin.m
//  hello
//
//  Created by seasu on 2015/7/29.
//
//

#import "LaunchNavigator.h"
#import <Cordova/CDV.h>


@implementation LaunchNavigator

NSString *TYPE_BAIDU = @"baidu";
NSString *TYPE_AMAP = @"amap";


- (void)doLaunch:(CDVInvokedUrlCommand*)command {
    
    CDVPluginResult *pluginResult = nil;
    
    NSString *type = [command argumentAtIndex:0];
    
    NSArray *dest = [command argumentAtIndex:1];
    NSArray *orig = [command argumentAtIndex:2];
    
    if (type == nil) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"navigator type missing"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    if ([@[TYPE_AMAP, TYPE_BAIDU] containsObject:type] == NO) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"navigator type not support1"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    
    @try {
        if (dest != nil && orig != nil) {
            [self navigate:type originName:orig[0] originLat:orig[2] originLng:orig[1] destinationName:dest[0] destinationLat:dest[2] destinationLng:dest[1] command:command];
        } else if (dest != nil){
            [self viewMap:type title:dest[0] lat:dest[2] lng:dest[1] command:command];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"navigator dest missing"];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        }
        }
    @catch (NSException *exception) {
    }
    @finally {
    }
    
}

- (void)viewMap:(NSString *)type
          title:(NSString *)name
            lat:(NSString *)lat
            lng:(NSString *)lng
        command:(CDVInvokedUrlCommand *)command {
    NSString *str = nil;
    if ([type isEqualToString:TYPE_BAIDU]) {
        str = [NSString stringWithFormat:@"baidumap://map/marker?location=%@,%@&title=%@&coord_type=gcj02&src=duduche|parking&content=目的地", lat, lng, name];
    } else if ([type isEqualToString:TYPE_AMAP]) {
        str = [NSString stringWithFormat:@"iosamap://viewMap?sourceApplication=duduche&poiname=%@&lat=%@&lon=%@&dev=0", name, lat, lng];
    }
    str = [str stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    [self openUrl:[NSURL URLWithString:str] command:command];
}

- (void)navigate:(NSString *)type
      originName:(NSString *)originName
       originLat:(NSNumber *)originLat
       originLng:(NSNumber *)originLng
 destinationName:(NSString *)destinationName
  destinationLat:(NSNumber *)destinationLat
  destinationLng:(NSNumber *)destinationLng
         command:(CDVInvokedUrlCommand *)command
{
    NSString *str = nil;
    if ([type isEqualToString:TYPE_BAIDU]) {
        str = [NSString stringWithFormat:@"baidumap://map/direction?origin=%@,%@&destination=%@,%@&mode=driving&src=duduche", originLat, originLng, destinationLat, destinationLng];
    } else if ([type isEqualToString:TYPE_AMAP]) {
        str = [NSString stringWithFormat:@"iosamap://path?sourceApplication=duduche&sname=%@&slat=%@&slon=%@dname=%@&dlat=%@&dlon=%@&dev=0&m=0&t=0", originName, originLat, originLng, destinationName, destinationLat, destinationLng];
    }
    str = [str stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    [self openUrl:[NSURL URLWithString:str] command:command];
}

- (void)openUrl:(NSURL *)url command:(CDVInvokedUrlCommand *)command{
    CDVPluginResult *pluginResult = nil;
    NSLog(@"%@", url.absoluteString);
    if ([[UIApplication sharedApplication] canOpenURL:url]) {
        [[UIApplication sharedApplication] openURL:url];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"ios application can not open."];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
}

@end
