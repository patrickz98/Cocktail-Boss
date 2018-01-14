//
//  ViewController.swift
//  Cocktail Boss
//
//  Created by patrick zierahn on 19.04.17.
//  Copyright © 2017 Patrick Zierahn. All rights reserved.
//

import UIKit

// class ViewController: UIViewController
class ViewController: UINavigationController
{
    override func viewDidLoad()
    {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        print("ViewController: viewDidLoad()")
        
//        self.navigationBar.backgroundColor = UIColor.orange
//        self.tabBarController?.tabBar.barTintColor = UIColor.orange
//        self.navigationController?.navigationBar.barTintColor = UIColor.orange
//        self.navigationController?.navigationBar.tintColor = UIColor.green
//        self.navigationController!.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.blueColor()]
        
//        let navigationBar = UINavigationBar.appearance()
//        navigationBar.tintColor = UIColor.white
//        navigationBar.barTintColor = UIColor.purple
//        navigationBar.titleTextAttributes = [NSForegroundColorAttributeName:UIColor.white]
        
//        self.view.backgroundColor = UIColor.blue

//        self.pushViewController(HelloViewController(), animated: true)
//        self.pushViewController(SampleViewController(), animated: true)
        self.pushViewController(MainViewController(), animated: true)
    }
    
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

