package com.abhishek.smartexpensetracker.core.utils


object FAQCont {
//    val faqs_question_answer_list = listOf<FAQ>(
//        FAQ(
//            question = "Is my data safe on Kagaz Mitra?",
//            answer = "Yes, your data is 100% secure. We use industry-standard encryption and privacy practices to ensure your personal information stays protected."
//        ),
//        FAQ(
//            question = "How to contact support?",
//            answer = "You can reach out via the 'Reach Us' section in the app."
//        ),
//        FAQ(
//            question = "Can I edit my submitted form?",
//            answer = "Yes, you can edit it before final submission."
//        ),
//        FAQ(
//            question = "Is video verification mandatory?",
//            answer = "Yes, it's a necessary part of our verification process."
//        ),
//        FAQ(
//            question = "What is the processing time?",
//            answer = "Processing takes 24-48 hours depending on document type."
//        ),
//        FAQ(
//            question = "What is Kagaz Mitra used for?",
//            answer = "Kagaz Mitra helps users fill out official forms, schedule support, and complete paperwork processes easily from their phone, without needing to visit government offices."
//        ),
//        FAQ(
//            question = "Can I get help if I don't understand a form?",
//            answer = "Absolutely! Kagaz Mitra provides step-by-step guidance and also lets you schedule a video call with our support team to assist you live."
//        ),
//        FAQ(
//            question = "Do I need to visit any office after using Kagaz Mitra?",
//            answer = "In most cases, no. Kagaz Mitra is designed to complete the process online. However, if physical verification is needed, we’ll notify you with next steps."
//        ),
//        FAQ(
//            question = "How do I track the status of my application?",
//            answer = "You can track the status anytime from the \"Status\" section in the app. You’ll also receive real-time updates via SMS and notifications."
//        ),
//        FAQ(
//            question = "What if I make a mistake while filling a form?",
//            answer = "No worries! You can review and edit your information before final submission. If already submitted, reach out to our support team for help."
//        ),
//        FAQ(
//            question = "Is Kagaz Mitra available in multiple languages?",
//            answer = "Yes, we support multiple Indian languages so you can comfortably use the app in your preferred language."
//        ),
//        FAQ(
//            question = "How much does it cost to use Kagaz Mitra?",
//            answer = "Many services are free. For premium services like expert help, there may be a small fee, which will be clearly shown before you make a payment."
//        ),
//        FAQ(
//            question = "How do I make a payment on Kagaz Mitra?",
//            answer = "You can pay securely via UPI, debit/credit cards, or other digital payment methods directly through the app."
//        ),
//        FAQ(
//            question = "Who can I contact if I need support?",
//            answer = "You can reach out via the “Reach Us” form in the app or schedule a video call. Our support team is here to help you with any issue."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra without an Aadhaar card?",
//            answer = "While many services require Aadhaar for verification, some services may still be available without it. We’ll clearly mention requirements before you proceed."
//        ),
//        FAQ(
//            question = "What documents do I need to keep ready?",
//            answer = "It depends on the service you’re using. Common documents include Aadhaar, PAN card, income certificate, electricity bill, etc. The app will guide you with a checklist for each form."
//        ),
//        FAQ(
//            question = "Will I receive proof or acknowledgment after form submission?",
//            answer = "Yes, once your form is submitted, you’ll get a digital receipt and a copy of your application. You can also download it anytime from the app."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra on multiple devices?",
//            answer = "Yes, you can log in with the same phone number on any device. Just verify with OTP to access your data securely."
//        ),
//        FAQ(
//            question = "What if I don’t receive the OTP during login?",
//            answer = "Make sure your mobile network is active. If still not received, tap \"Resend OTP\" or contact our support team for quick help."
//        ),
//        FAQ(
//            question = "Is there a limit to how many forms I can fill?",
//            answer = "No, you can fill as many forms as you need. Kagaz Mitra is designed to support you throughout your entire paperwork journey."
//        ),
//        FAQ(
//            question = "Can I cancel or reschedule a video support session?",
//            answer = "Yes, you can cancel or reschedule your video call from the “Schedule Support” section. Just make sure to do it at least 30 minutes in advance."
//        ),
//        FAQ(
//            question = "Are my payments on Kagaz Mitra secure?",
//            answer = "Yes, all transactions are encrypted and handled through secure, trusted payment gateways. We never store your card or UPI details."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra save my form data automatically?",
//            answer = "Yes, your progress is auto-saved as you fill the form, so you can come back and continue anytime without losing your data."
//        ),
//        FAQ(
//            question = "Can I get a refund if something goes wrong?",
//            answer = "Yes, if a paid service was not delivered due to a valid issue, you may be eligible for a refund as per our refund policy. Contact support for assistance."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra work on both Android and iOS?",
//            answer = "Currently, Kagaz Mitra is available on Android. iOS support is coming soon — stay tuned for updates."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra without signing up?",
//            answer = "Some features may be previewed without signing in, but to fill forms, schedule support, or save progress, you’ll need to create an account using your phone number."
//        ),
//        FAQ(
//            question = "Will Kagaz Mitra notify me about application deadlines?",
//            answer = "Yes, for time-sensitive services, we send you reminders and alerts so you never miss a government deadline."
//        ),
//        FAQ(
//            question = "What happens after I submit a form through Kagaz Mitra?",
//            answer = "Your form is verified and submitted to the respective authority or partner system. You’ll receive updates as your application progresses."
//        ),
//        FAQ(
//            question = "How do I update my profile details in the app?",
//            answer = "Go to the Profile section from the home screen and tap on the field you want to edit — like name, photo, or email. Remember to save your changes."
//        ),
//        FAQ(
//            question = "Is my video call with support recorded?",
//            answer = "No, your video calls are not recorded. They’re conducted securely and privately, strictly to assist you with form filling or doubts."
//        ),
//        FAQ(
//            question = "How can I delete my account or data from Kagaz Mitra?",
//            answer = "You can go to Settings > Privacy & Security > Delete Account to request deletion of your data and account. It may take up to 48 hours to process."
//        ),
//        FAQ(
//            question = "How can I give feedback or report a bug in the app?",
//            answer = "We’d love to hear from you! Go to the ‘Reach Us’ section and choose ‘Feedback’ or ‘Report a Problem’ to submit your message."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra for my family members?",
//            answer = "Yes, you can fill forms on behalf of others — just make sure to enter their details correctly. We’ll guide you through the process for each individual."
//        ),
//        FAQ(
//            question = "Can I save partially filled forms and complete them later?",
//            answer = "Yes, Kagaz Mitra automatically saves your form progress. You can return anytime and resume from where you left off."
//        ),
//        FAQ(
//            question = "What if I entered incorrect information by mistake?",
//            answer = "Before final submission, you’ll have the chance to review and edit all information. If submitted already, contact support to explore correction options."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra support rural and remote areas?",
//            answer = "Yes! Kagaz Mitra is designed to work in areas with low bandwidth and supports local languages to ensure accessibility across India."
//        ),
//        FAQ(
//            question = "Can I upload scanned documents or only photos?",
//            answer = "You can upload both scanned documents (PDF, JPG, PNG) and photos directly from your phone’s gallery or camera."
//        ),
//        FAQ(
//            question = "What if my photo or document upload fails?",
//            answer = "Ensure you have a stable internet connection and that the file size is within the limit (usually 5MB). If the issue continues, try compressing the file or contact support."
//        ),
//        FAQ(
//            question = "How long does it take for my form to be processed?",
//            answer = "Processing times vary based on the type of service. Most applications are reviewed within 2–5 working days. You’ll receive real-time updates in the app."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra in offline mode?",
//            answer = "Basic form viewing and saved data access may be available offline, but for submitting forms, video calls, or updates, an internet connection is required."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra support government scheme applications?",
//            answer = "Yes, Kagaz Mitra helps you apply for various government schemes by simplifying the form-filling and documentation process."
//        ),
//        FAQ(
//            question = "How do I know if a service is genuine?",
//            answer = "All services on Kagaz Mitra are verified and authentic. We work with trusted partners and official processes only."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra for business or commercial document help?",
//            answer = "Currently, Kagaz Mitra is focused on individual and household documentation. Support for MSMEs and business-related services is coming soon."
//        ),
//        FAQ(
//            question = "Can I receive printed documents via courier?",
//            answer = "At this time, Kagaz Mitra offers digital downloads only. If courier service is available for your region, we’ll display that option during submission."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra store my Aadhaar or PAN details?",
//            answer = "No, your sensitive identity details are not stored permanently. We process them securely only when required for submission and verification."
//        ),
//        FAQ(
//            question = "Is customer support available on weekends?",
//            answer = "Yes, support is available 7 days a week from 9 AM to 7 PM. For urgent queries, you can also reach out through the app chat option."
//        ),
//        FAQ(
//            question = "How can I share Kagaz Mitra with others?",
//            answer = "You can tap on the “Share App” option in the menu to invite friends and family via WhatsApp, SMS, or social media."
//        ),
//        FAQ(
//            question = "Will Kagaz Mitra remind me of upcoming renewals or expiry dates?",
//            answer = "Yes! For services like certificate renewals, we’ll send you timely reminders so you can act before deadlines."
//        ),
//        FAQ(
//            question = "Can I use Kagaz Mitra on a tablet?",
//            answer = "Yes, Kagaz Mitra works smoothly on Android tablets with the latest OS version."
//        ),
//        FAQ(
//            question = "Does the app support dark mode?.",
//            answer = "Yes, Kagaz Mitra supports both light and dark modes. It adapts based on your system settings."
//        ),
//        FAQ(
//            question = "Is there a help tutorial when I use the app for the first time?",
//            answer = "Yes, you’ll see a quick onboarding guide when you first launch Kagaz Mitra to help you understand how the app works."
//        ),
//        FAQ(
//            question = "What if I accidentally close the app while filling a form?",
//            answer = "No worries! Your progress is saved automatically, and you can resume when you open the app again."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra verify my documents before submission?",
//            answer = "Yes, our team checks for completeness and clarity before final submission to ensure accuracy."
//        ),
//        FAQ(
//            question = "Can I use my email ID instead of a phone number to log in?",
//            answer = "Currently, login is only supported through your mobile number with OTP verification."
//        ),
//        FAQ(
//            question = "How can I check if my form has been accepted?",
//            answer = "You can track the application status in real-time under the “Status” section of the app."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra have a desktop/web version?",
//            answer = "Not yet. We’re currently mobile-only, but a web version is planned for future updates."
//        ),
//        FAQ(
//            question = "Are there any hidden charges in Kagaz Mitra?",
//            answer = "No. All charges (if any) are clearly shown before you confirm any payment."
//        ),
//        FAQ(
//            question = "Can I chat with a support agent?",
//            answer = "Yes, real-time chat support is available within the app during business hours."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra help me with form translations?",
//            answer = "Yes, we display forms in regional languages and explain complex fields in simple terms."
//        ),
//        FAQ(
//            question = "What if the government website is down?",
//            answer = "We’ll queue your submission and notify you once the portal is back online."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra offer discounts or referral bonuses?",
//            answer = "Yes, we run occasional offers and referral rewards. Check the “Offers” section in the app."
//        ),
//        FAQ(
//            question = "What is the minimum Android version required to use Kagaz Mitra?",
//            answer = "Kagaz Mitra supports Android 7.0 (Nougat) and above."
//        ),
//        FAQ(
//            question = "How is Kagaz Mitra different from a cyber café or agent?",
//            answer = "With Kagaz Mitra, you get digital assistance, transparent pricing, privacy, and the convenience of completing everything from home."
//        ),
//        FAQ(
//            question = "Can I track the history of all the forms I’ve submitted?",
//            answer = "Yes, go to “My Applications” to view all your past submissions and their status."
//        ),
//        FAQ(
//            question = "Will I get a notification once the final certificate or document is ready?",
//            answer = "Yes, you’ll receive a push notification and SMS when your document is ready to download."
//        ),
//        FAQ(
//            question = "Is Kagaz Mitra suitable for senior citizens?",
//            answer = "Yes! The app is designed with a simple interface, and we also offer live video assistance to help elderly users."
//        ),
//        FAQ(
//            question = "Can I change the language of the app later?",
//            answer = "Yes, you can switch languages anytime from the Settings > Language section."
//        ),
//        FAQ(
//            question = "How do I know which documents are required for a service?",
//            answer = "When you start a service, we’ll provide a checklist of required documents specific to that service."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra help me correct my Aadhaar details?",
//            answer = "We can guide you through the correction process, but final approval depends on UIDAI's rules."
//        ),
//        FAQ(
//            question = "Can I use the app for birth/death certificate services?",
//            answer = "Yes, Kagaz Mitra supports applications for various civil documents, including birth and death certificates (availability varies by region)."
//        ),
//        FAQ(
//            question = "Is the video call feature chargeable?",
//            answer = "One free call may be included for some services. Additional or premium video calls may have a nominal fee, shown in advance."
//        ),
//        FAQ(
//            question = "Will the app log me out automatically?",
//            answer = "To ensure security, the app logs out inactive sessions after a certain time. You can log back in with OTP."
//        ),
//        FAQ(
//            question = "Can I download a copy of submitted forms?",
//            answer = "Yes, all submitted forms and receipts are available for download in PDF format from the “My Applications” section."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra notify me about new government schemes?",
//            answer = "Yes, we regularly update the app with information on new schemes and eligibility so you never miss an opportunity."
//        ),
//        FAQ(
//            question = "What if I forget to complete a form I started?",
//            answer = "The app will remind you to complete your pending forms so you don’t miss out on important services."
//        ),
//        FAQ(
//            question = "Can I receive my documents by email?",
//            answer = "Yes, after submission, you can choose to email the documents to yourself directly from the app."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra store my credit/debit card details?",
//            answer = "No, Kagaz Mitra does not store any sensitive payment information. All payments are processed via secure gateways."
//        ),
//        FAQ(
//            question = "Can I report a fraudulent service or form?",
//            answer = "Yes, you can report issues from the 'Reach Us' section. We take such concerns seriously and investigate immediately."
//        ),
//        FAQ(
//            question = "Does the app provide updates for state-level services?",
//            answer = "Yes, Kagaz Mitra includes support for both central and state-level forms and services depending on your region."
//        ),
//        FAQ(
//            question = "How do I switch between users or family members in the app?",
//            answer = "You can add multiple profiles in the app and switch between them while filling out different forms."
//        ),
//        FAQ(
//            question = "What types of services are available on Kagaz Mitra?",
//            answer = "We offer assistance with certificates, scheme applications, ID updates, form filling, and more."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra help with passport-related services?",
//            answer = "We can guide you through the online passport application or renewal process, but biometric appointments must be done in person."
//        ),
//        FAQ(
//            question = "How can I know if my form has errors before submission?",
//            answer = "Our system checks common errors and missing fields and alerts you before submission to help avoid rejections."
//        ),
//        FAQ(
//            question = "Is it safe to upload my Aadhaar or PAN card in the app?",
//            answer = "Yes, document uploads are encrypted and used only for the intended service. We never misuse or share your data."
//        ),
//        FAQ(
//            question = "Can I preview the filled form before submitting?",
//            answer = "Yes, you can preview and review all details before final submission to ensure accuracy."
//        ),
//        FAQ(
//            question = "Will Kagaz Mitra work on older phones?",
//            answer = "Yes, as long as your device runs Android 7.0 or higher with basic storage and network capabilities."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra offer voice support for users with disabilities?",
//            answer = "Voice instructions and accessible layouts are part of our roadmap to improve usability for everyone."
//        ),
//        FAQ(
//            question = "How can I resend my document if the recipient didn’t get it?",
//            answer = "You can easily resend documents from the 'My Applications' section to email or WhatsApp."
//        ),
//        FAQ(
//            question = "Are all services in Kagaz Mitra available 24/7?",
//            answer = "Form filling and document upload are available 24/7, but live support is available during business hours."
//        ),
//        FAQ(
//            question = "How will I know if my form submission failed?",
//            answer = "You will receive a notification and see the error reason in the 'Status' tab, along with instructions to fix it."
//        ),
//        FAQ(
//            question = "Can I cancel a submitted application?",
//            answer = "Once submitted to the official portal, cancellations depend on government rules. Contact support for options."
//        ),
//        FAQ(
//            question = "Can I get printed copies of my submitted documents?",
//            answer = "While Kagaz Mitra provides digital downloads, you can also print them at your convenience or at nearby print centers."
//        ),
//        FAQ(
//            question = "Does Kagaz Mitra keep a backup of my documents?",
//            answer = "Yes, your submitted documents are securely stored and accessible from your account anytime."
//        ),
//        FAQ(
//            question = "Can I edit my form after submission?",
//            answer = "Edits after submission are not possible unless the form is rejected or under correction period. You can always reapply or contact support."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra help me with job-related government forms?",
//            answer = "Yes, we support many job-related forms like employment cards, exam applications, and more."
//        ),
//        FAQ(
//            question = "Is Kagaz Mitra approved by any government body?",
//            answer = "Kagaz Mitra is a private digital assistant that works with publicly available services and ensures transparency and legal compliance."
//        ),
//        FAQ(
//            question = "What do I do if my video call failed to connect?",
//            answer = "Check your internet connection and retry. You can also reschedule the call from the support section."
//        ),
//        FAQ(
//            question = "Can I fill forms in advance for a future date?",
//            answer = "Yes, you can complete forms and save them as drafts or submit them closer to the intended date."
//        ),
//        FAQ(
//            question = "Is there an emergency support option?",
//            answer = "Yes, for time-sensitive services, we offer a priority support option during business hours."
//        ),
//        FAQ(
//            question = "Can Kagaz Mitra help with voter ID applications?",
//            answer = "Yes, we guide users through the process of applying, updating, or checking status of voter ID applications."
//        ),
//        FAQ(
//            question = "How does Kagaz Mitra protect my privacy?",
//            answer = "We follow strict data protection policies, use encrypted servers, and never share your personal info without consent."
//        ),
//        FAQ(
//            question = "Can I request a callback from the support team?",
//            answer = "Yes, you can request a callback via the support section, and our team will get in touch within business hours."
//        ),
//        FAQ(
//            question = "Is Kagaz Mitra available in all states of India?",
//            answer = "Yes, we support services in all Indian states, though availability may vary depending on the local government system."
//        )
//    )
}