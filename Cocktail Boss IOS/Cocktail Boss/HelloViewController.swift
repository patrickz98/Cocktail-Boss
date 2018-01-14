//
//  HelloViewController.swift
//  Cocktail Boss
//
//  Created by patrick zierahn on 22.04.17.
//  Copyright © 2017 Patrick Zierahn. All rights reserved.
//

import UIKit

class HelloViewController: UIViewController
{
    override func viewDidLoad()
    {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.navigationController?.navigationBar.topItem?.title = "Title here"
        self.view.backgroundColor = UIColor.green
        
        let button = UIButton(type: UIButtonType.system)
        // button.center = self.view.center
        button.setTitle("Exit Button", for: UIControlState.normal)
        button.sizeToFit()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(buttonAction(sender:)), for: UIControlEvents.touchUpInside)
        
        self.view.addSubview(button)
        
        let widthConstraint = NSLayoutConstraint(
            item: button,
            attribute: NSLayoutAttribute.width,
            relatedBy: NSLayoutRelation.equal,
            toItem: nil,
            attribute: NSLayoutAttribute.notAnAttribute,
            multiplier: 1.0,
            constant: 250)
        
        let heightConstraint = NSLayoutConstraint(
            item: button,
            attribute: NSLayoutAttribute.height,
            relatedBy: NSLayoutRelation.equal,
            toItem: nil,
            attribute: NSLayoutAttribute.notAnAttribute,
            multiplier: 1.0,
            constant: 100)
        
        let xConstraint = NSLayoutConstraint(
            item: button,
            attribute: NSLayoutAttribute.centerX,
            relatedBy: NSLayoutRelation.equal,
            toItem: self.view,
            attribute: NSLayoutAttribute.centerX,
            multiplier: 1,
            constant: 0)
        
        let yConstraint = NSLayoutConstraint(
            item: button,
            attribute: NSLayoutAttribute.centerY,
            relatedBy: NSLayoutRelation.equal,
            toItem: self.view,
            attribute: NSLayoutAttribute.centerY,
            multiplier: 1,
            constant: 0)
        
        NSLayoutConstraint.activate([widthConstraint, heightConstraint, xConstraint, yConstraint])
        
        
        self.navigationController?.isToolbarHidden = false
        
        var items = self.navigationController?.toolbar.items
        
        items?.append(
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.add, target: nil, action: nil)
        )
    }
    
    func buttonAction(sender: UIButton!)
    {
        print("Exit Button")
//        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }

    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
