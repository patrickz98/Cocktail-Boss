//
//  SampleViewController.swift
//  Cocktail Boss
//
//  Created by patrick zierahn on 24.04.17.
//  Copyright © 2017 Patrick Zierahn. All rights reserved.
//

import UIKit

class SampleViewController: UIViewController
{
    func buttonAction(sender: UIButton!)
    {
        print("Button tapped")
        
        let newView = HelloViewController()
        // self.present(newView, animated: true, completion: nil)
        
//        let callback = {
//            print("Done🔨")
//        }
//        
//        self.present(newView, animated: true, completion: callback)
        
        self.navigationController?.pushViewController(newView, animated: true)
    }

    override func viewDidLoad()
    {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        print("SampleViewController: viewDidLoad()")

        self.view.backgroundColor = UIColor.darkGray
        self.view.autoresizesSubviews = true
        
        let button = UIButton(type: UIButtonType.system)
        button.setTitle("Test Button", for: UIControlState.normal)
        button.sizeToFit()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(buttonAction(sender:)), for: UIControlEvents.touchUpInside)
        button.center = CGPoint(x: self.view.center.x, y: 100)
        
        self.view.addSubview(button)
        
        let items = ["Purple", "Green", "Blue"]
        let segmentedControl = UISegmentedControl(items: items)
        segmentedControl.selectedSegmentIndex = 0
        segmentedControl.center = CGPoint(x: self.view.center.x, y: 200)
        
        self.view.addSubview(segmentedControl)
        
        
        let slider = UISlider()
        slider.frame = CGRect(x: 20, y:  300, width: self.view.frame.width - 40, height: 50)
        
        self.view.addSubview(slider)
        
        
        let mySwitch = UISwitch()
        mySwitch.isOn = true
        mySwitch.center = CGPoint(x: self.view.center.x, y: 400)
        self.view.addSubview(mySwitch)
        
//        self.navigationController?.hidesBarsOnSwipe = true        
        
        self.navigationItem.title = "Sample View"

        let testButton = UIBarButtonItem()
        testButton.style = .plain
        testButton.title = "ABC"
        
        self.navigationItem.rightBarButtonItem = testButton
        
        let backItem = UIBarButtonItem()
        backItem.title = "Back"
        self.navigationItem.backBarButtonItem = backItem // This will show in the next view controller being pushed

//        self.navigationItem.backBarButtonItem?.title = "Back"

//        let backItem = UIBarButtonItem()
//        backItem.title = "Back"
//        self.navigationItem.backBarButtonItem = backItem // This will show in the next view controller being pushed
        
//        self.navigationController?.navigationBar.backItem?.title = "Back"
        
//        self.navigationController?.navigationBar.topItem?.title = "Sample View"
//        self.navigationController?.navigationBar.tintColor = UIColor.orange
//        self.navigationController?.navigationBar.topItem?.title = "Back"
    }
    
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
