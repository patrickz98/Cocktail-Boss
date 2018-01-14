//
//  MainViewController.swift
//  Cocktail Boss
//
//  Created by patrick zierahn on 24.04.17.
//  Copyright © 2017 Patrick Zierahn. All rights reserved.
//

import UIKit

class MainViewController: UIViewController
{
////    var navigationController: ViewController
//    
//    init(navigationController: ViewController)
//    {
////        self.navigationController = navigationController
//        navigationController.pus
//    }
    
    func buttonAction(sender: UIButton!)
    {
//        self.navigationItem.title = nil
        self.navigationController?.pushViewController(SampleViewController(), animated: true)
//        self.navigationController?.popToViewController(SampleViewController(), animated: true)
//        self.navigationController?.navigationBar.backgroundColor = UIColor.green
    }

    override func viewDidLoad()
    {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        print("MainViewController: viewDidLoad()")

        self.view.backgroundColor = UIColor.white
        
        let button = UIButton(type: UIButtonType.system)
        button.center = self.view.center
        button.setTitle("Next", for: UIControlState.normal)
        button.sizeToFit()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.addTarget(self, action: #selector(buttonAction(sender:)), for: UIControlEvents.touchUpInside)

        self.view.addSubview(button)
        
//        self.navigationController?.hidesBarsOnSwipe = true
        self.navigationItem.title = "Main View"
    }
    
//    override func viewWillAppear(_ animated: Bool)
//    {
//        print("MainViewController: viewWillAppear()")
//        self.navigationItem.title = "Main View"
//    }
    
    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
